package it.polimi.GC13.view.TUI;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.View;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;
import java.util.stream.Collectors;

public class TUI implements View {
    private ServerInterface virtualServer;
    private String nickname;
    private String gameName;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard = new LinkedList<>();
    private boolean myTurn = false;
    private int turnPlayed = 0;
    private final Map<String, Integer> playersScore = new ConcurrentHashMap<>();
    private final Map<String, Position> playersPosition = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> goldCardsAvailable = new ConcurrentHashMap<>();
    private final Map<Integer, Boolean> resourceCardsAvailable = new ConcurrentHashMap<>();
    private final Map<String, BoardView> playersBoard = new ConcurrentHashMap<>();
    private final Map<String, EnumMap<Resource, Integer>> playersCollectedResources = new ConcurrentHashMap<>();
    private final List<String> gamesLog = new ArrayList<>();
    private boolean cooking = false;
    private int choice = 0;
    private final Printer printer = new Printer();
    private final Reader newReader = new Reader();
    private final Map<String, List<String>> chat = new HashMap<>();
    private boolean newMessage = false;
    private boolean connectionOpen = true;

    public TUI() {
        new Thread(this.newReader, "READER").start();
    }

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void setVirtualServer(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
    }

    @Override
    public void startView() {
        this.printer.intro();
        this.checkForExistingGame();
    }

    /**
     * used to update players hand in TUI
     */
    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if (playerNickname.equals(this.nickname)) {
            synchronized (this.hand) {
                this.hand.clear();
                this.hand.addAll(availableCard);
            }
        } else {
            this.gamesLog.add(playerNickname + " has modified his hand");
        }
    }

    @Override
    public void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial) {
        this.goldCardsAvailable.clear();
        this.goldCardsAvailable.putAll(goldCardSerial);
    }

    @Override
    public void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceFacedUpSerial) {
        this.resourceCardsAvailable.clear();
        this.resourceCardsAvailable.putAll(resourceFacedUpSerial);
    }

    @Override
    public void checkForExistingGame() {
        this.nickname = null;
        this.gameName = null;
        this.virtualServer.sendMessageFromClient(new CheckForExistingGameMessage());
    }

    /**
     * JOINING PHASE
     * [1] create new game
     * [2] join an existing one
     */
    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) {
        if (gameNameWaitingPlayersMap.isEmpty()) {
            System.out.println("There is no existing game");
            createNewGame();
        } else {
            this.choice = userIntegerInput("There are existing games\n\t[1] to create a new Game\n\t[2] to join an existing Game\nYour choice", (num) -> {
                if (num == null) {
                    return false;
                }
                if (num < 1 || num > 2) {
                    System.out.println("please enter either [1] or [2]");
                    return false;
                }
                return true;
            });
            if (this.choice == -1) {
                return;
            }
            if (this.choice == 1) {
                //player wants to create a new game
                this.createNewGame();
            } else {
                //player can and wants to join an existing game
                joinExistingGame(gameNameWaitingPlayersMap);
            }
            this.choice = 0;
        }
    }

    private void createNewGame() {
        String gameName;
        int playersNumber;

        //asking for all the contents of the message
        this.nickname = userStringInput("Choose your nickname", (input) -> input != null && !input.isBlank());
        if (this.nickname == null) {
            return;
        }
        gameName = userStringInput("Choose a name for the new Game", (input) -> input != null && !input.isBlank());
        if (gameName == null) {
            return;
        }
        playersNumber = userIntegerInput("Choose Number of players in the game [min 2, max 4]",
                (num) -> {
                    if (num == null) {
                        return false;
                    }
                    if (num < 2 || num > 4) {
                        System.out.println("please enter either [2], [3] or [4]");
                        return false;
                    }
                    return true;
                });
        if (playersNumber == -1) {
            return;
        }
        this.virtualServer.sendMessageFromClient(new CreateNewGameMessage(this.nickname, playersNumber, gameName));
    }

    private void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) {
        String gameName;
        this.nickname = userStringInput("Choose your nickname", (input) -> input != null && !input.isBlank());
        if (this.nickname == null) {
            return;
        }
        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>gameName: [" + string + "] --|players in waiting room: " + numCurrPlayer + "|"));
        gameName = userStringInput("Select the game to join using its name", (input) -> {
            if (input == null || input.isBlank()) {
                return false;
            }
            if (!gameNameWaitingPlayersMap.containsKey(input)) {
                System.out.println("There is no such game");
                return false;
            }
            return true;
        });
        if (gameName == null) {
            return;
        }
        //massage is ready to be sent
        this.virtualServer.sendMessageFromClient(new AddPlayerToGameMessage(this.nickname, gameName));
    }

    /**
     * SETUP PHASE
     * token choice when all players joined the game
     * waiting when readPlayers < neededPlayers
     */
    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) {
        StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");
        if (readyPlayers == neededPlayers) {
            if (this.gameName == null) {
                this.gameName = gameName;
            }
            tokenColorList.stream().map(TokenColor::toString).forEach(joiner::add);
            System.out.println("\n--- SETUP PHASE [1/2]---");
            String tokenColorChosen = userStringInput("Choose your token color " + joiner + "\nColor", (input) -> {
                if (input == null || input.isBlank()) {
                    return false;
                }
                if (tokenColorList.stream().anyMatch(tc -> tc.name().equalsIgnoreCase(input))) {
                    return true;
                } else {
                    System.out.println("Color not valid.");
                    return false;
                }
            });
            if (tokenColorChosen == null) {
                return;
            }
            this.virtualServer.sendMessageFromClient(new TokenChoiceMessage(TokenColor.valueOf(tokenColorChosen.toUpperCase())));
        } else {
            System.out.println("--|players in waiting room: " + readyPlayers + "/" + neededPlayers);
        }
    }

    /**
     * SETUP PHASE methods to the player
     * startCardSetupPhase to chose which side to place your start card
     */
    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) {
        if (playerNickname.equals(this.nickname)) {
            System.out.println("You chose " + tokenColor + " token\n");
            System.out.println("--- SETUP PHASE [2/2] ---");
            System.out.println("--- START CARD ---");
            this.printer.showHand(this.hand);
            this.choice = userIntegerInput("Choose which side you would like to place your start card:\n\t[1] FRONT\n\t[2] BACK\nChoice", (num) -> {
                if (num == null) {
                    return false;
                }
                if (num < 1 || num > 2) {
                    System.out.println("please enter either [1] or [2]");
                    return false;
                }
                return true;
            });
            if (choice == -1) {
                return;
            }

            this.virtualServer.sendMessageFromClient(new PlaceStartCardMessage(this.choice != 1));
            this.choice = 0;
        }
        this.gamesLog.add(playerNickname + " chose " + tokenColor + " token");
    }

    /**
     * NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
     * OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn, List<Coordinates> availableCells) {
        String message = playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn;

        if (!this.playersBoard.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add(message);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);

        if (playerNickname.equals(this.nickname)) {
            if (serialCardPlaced <= 80) {
                System.out.println(message);
                this.showHomeMenu(); // show menu after placing a card
            } else {
                System.out.println(message + ".\nWaiting for other players...");
            }
        }

    }

    /**
     * NOTIFY THE CLIENTS ABOUT THE COMMON OBJECTIVE CARD
     */
    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }

    /**
     * METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            this.printer.showObjectiveCard("--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
            this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", privateObjectiveCards);
            while (!privateObjectiveCards.contains(choice)) {
                this.choice = userIntegerInput("\nChoose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]",
                        (num) -> {
                            if (num == null) {
                                return false;
                            }
                            if (!privateObjectiveCards.contains(num)) {
                                System.out.println("please enter one of the following objective cards: [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]");
                                return false;
                            }
                            return true;
                        });
                if (choice == -1) {
                    return;
                }
            }
            this.virtualServer.sendMessageFromClient(new ChoosePrivateObjectiveCardMessage(choice));
            this.choice = 0;
        }
    }

    /**
     * NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
    @Override
    public void setPrivateObjectiveCard(String playerNickname, int serialPrivateObjectiveCard, int readyPlayers, int neededPlayers) {
        if (playerNickname.equals(this.nickname)) {
            this.serialPrivateObjectiveCard = serialPrivateObjectiveCard;
            String message = "Your private objective card is " + serialPrivateObjectiveCard;
            System.out.println(message + ".");
            this.gamesLog.add(message);
        } else {
            this.gamesLog.add(playerNickname + " chose his private objective card");
        }
        if (this.serialPrivateObjectiveCard != 0 && readyPlayers != neededPlayers) {
            System.out.println("--|players that chose objective card: " + readyPlayers + "/" + neededPlayers);
        }
    }

    /**
     * METHOD TO REQUEST DRAW CARD
     */
    @Override
    public void drawCard() throws GenericException {
        System.out.println("\n--- DRAWABLE CARDS ---");
        System.out.println("--- Gold Deck ---");
        this.printer.showDrawableCards(this.goldCardsAvailable);
        System.out.println("\n--- Resource Deck ---");
        this.printer.showDrawableCards(this.resourceCardsAvailable);
        if (this.myTurn && this.hand.size() == 2) {
            this.choice = userIntegerInput("Choose the card to withdraw", (num) -> {
                if (num == null) {
                    return false;
                }
                return this.goldCardsAvailable.containsKey(num) || this.resourceCardsAvailable.containsKey(num);
            });
            if (choice == -1) {
                return;
            }
            this.virtualServer.sendMessageFromClient(new DrawCardFromDeckMessage(choice));

            this.choice = 0;
            this.turnPlayed++;
        } else {
            System.out.println("You cannot draw from the deck if it is not your turn or you didn't place one card on the board.");
            this.showHomeMenu();
        }
    }

    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            //System.out.println("\u001B[31mLaunching an exception\u001B[0m");
            System.out.println("\u001B[33m" + onInputExceptionMessage.getErrorMessage() + "\u001B[0m");
            onInputExceptionMessage.methodToRecall(this);
        }
        this.gamesLog.add(onInputExceptionMessage.getErrorMessage());
    }

    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {
        String cellCoordinates = availableCells.stream()
                .map(cell -> "(" + cell.getX() + ", " + cell.getY() + ")")
                .collect(Collectors.joining("\n"));
        System.out.println("Available cells are:\n" + cellCoordinates + ".");
        this.showHomeMenu();
    }

    /**
     * METHOD USED TO GIVE EACH USER VISIBILITY OF PLAYERS ORDER
     */
    @Override
    public void setPlayersOrder(Map<String, Position> playerPositions) {
        this.playersPosition.putAll(playerPositions);
    }

    @Override
    public void onSetLastTurn(String playerNickname) {
        int lastTurnSetterPosition = this.playersPosition.get(playerNickname).getIntPosition();
        System.out.println(nickname + " has reached 20 points. The will be another turn for all players after the current turn ends.");
        if (lastTurnSetterPosition < this.playersPosition.size()) {
            System.out.println("Players to finish the turn: " + this.playersPosition.entrySet().stream().filter(entry -> entry.getValue().getIntPosition() > lastTurnSetterPosition).map(Map.Entry::getKey).collect(Collectors.joining(" ")));
        }

        this.menuOptions();
        System.out.print("Your choice: ");
    }

    /**
     * METHOD USED TO PLACE CARD ON THE BOARD
     */
    @Override
    public void placeCard() throws GenericException {
        if (this.myTurn && this.hand.size() == 3) {
            int X, Y, serialCardToPlace;
            this.playersBoard.get(this.nickname).printBoard();
            System.out.println();
            this.printer.collectedResource(this.playersCollectedResources.get(this.nickname));
            this.printer.showHand(this.hand);
            serialCardToPlace = userIntegerInput("Enter serial card", (num) -> {
                if (num == null) {
                    return false;
                }
                if (!this.hand.contains(num)) {
                    System.out.print("You don't have the selected card. Available cards are:");
                    this.hand.forEach(card -> System.out.print(" " + card));
                    System.out.println();
                    return false;
                }
                return true;
            });
            if (serialCardToPlace == -1) {
                return;
            }
            //TODO: can we put negative numbers as coordinates?
            X = userIntegerInput("Enter X coordinate", Objects::nonNull);
            if (X == -1) {
                return;
            }
            Y = userIntegerInput("Enter Y coordinate", Objects::nonNull);
            if (Y == -1) {
                return;
            }
            this.choice = userIntegerInput("Enter\n\t[1] for FRONT\n\t[2] for BACK\nChoice", (num) -> {
                if (num == null) {
                    return false;
                }
                if (!(num == 1 || choice == 2)) {
                    System.out.println("please enter either [1] or [2]");
                    return false;
                }
                return true;
            });
            if (choice == -1) {
                this.choice = 0;
                return;
            }
            this.virtualServer.sendMessageFromClient(new PlaceCardMessage(serialCardToPlace, this.choice == 2, X, Y));
            this.choice = 0;
        } else if (!this.myTurn) {
            System.out.println("It's not your turn");
            this.showHomeMenu();
        } else {
            System.out.println("You have already placed a card. You need to draw a card to pass the turn.");
            this.showHomeMenu();
        }
    }

    @Override
    public void updatePlayerScore(String playerNickname, int newPlayerScore) {
        if (!this.playersScore.containsKey(playerNickname)) {
            this.playersScore.put(playerNickname, newPlayerScore);
        } else {
            this.playersScore.replace(playerNickname, newPlayerScore);
        }
    }

    /**
     * method to save new a message in the chat
     *
     * @param sender    message sender
     * @param recipient message recipient
     * @param message   string that contains the message itself
     */
    @Override
    public void onNewMessage(String sender, String recipient, String message) {
        // CASE A -> I AM THE RECIPIENT
        if (recipient.equals(this.nickname)) {
            this.registerChatMessage(sender, message);
        }
        // CASE B -> BROADCAST MESSAGE
        else if (recipient.equals("global")) {
            this.registerChatMessage("global", message);
        }
        // CASE C -> I AM THE SENDER
        else if (sender.equals(this.nickname)) {
            this.registerChatMessage(recipient, message);
        }
    }

    @Override
    public void gameOver(Set<String> winner) {
        if (winner.stream().anyMatch(winnerNickname -> winnerNickname.equals(this.nickname))) {
            this.printer.winnerString();
        } else {
            this.printer.loserString();
        }
        this.printer.showPlayersScore(this.playersScore);
    }

    @Override
    public void reconnectToGame() {
        this.virtualServer.sendMessageFromClient(new ReconnectPlayerToGameMessage(this.gameName, this.nickname));
    }

    @Override
    public void onReconnectToGame() {
        this.showHomeMenu();
    }

    @Override
    public void updateCollectedResource(String playerNickname, EnumMap<Resource, Integer> collectedResources) {
        if (!this.playersCollectedResources.containsKey(playerNickname)) {
            this.playersCollectedResources.put(playerNickname, collectedResources);
        } else {
            this.playersCollectedResources.entrySet()
                    .stream()
                    .filter(playersMap -> playersMap.getKey().equals(playerNickname))
                    .forEach(playersMap -> playersMap.getValue().entrySet()
                            .stream()
                            .filter(entry -> !entry.getValue().equals(collectedResources.get(entry.getKey())))
                            .forEach(entry -> entry.setValue(collectedResources.get(entry.getKey()))));
        }
    }

    /**
     * method to send a message to any player or to everyone
     */
    private void sendMessage() throws GenericException {
        this.cooking = true;
        String playerChosen;
        String message;
        playerChosen = userStringInput("Choose who to send the message to: [" + (String.join("], [", this.playersBoard.keySet()) + "]") + " or [global]\nPlayer",
                (input) -> {
                    if (input == null || input.isBlank()) {
                        return false;
                    }
                    if (!(this.playersBoard.containsKey(input) || input.equals("global"))) {
                        System.out.println("playerName not found");
                        return false;
                    }
                    return true;
                });
        if (playerChosen == null) {
            return;
        }
        message = userStringInput("Write down your message", (input) -> input != null && !input.isBlank());
        if (message == null) {
            return;
        }
        this.virtualServer.sendMessageFromClient(new NewMessage(this.nickname, playerChosen, message));
    }

    /**
     * NOTIFY RESPECTIVE CLIENT WHEN IT'S THEIR TURN
     * OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
            // prints for the first time the main menu for the first player
            if (this.myTurn && this.turnPlayed == 0 && this.playersPosition.get(this.nickname).equals(Position.FIRST)) {
                this.showHomeMenu();
            } else if (!this.myTurn && this.turnPlayed == 0) {
                // prints for the first time the main menu for all other players
                this.showHomeMenu();
            } else {
                if (!this.myTurn) {
                    // notifies the player has passed the turn
                    System.out.println("You have passed the turn");
                    this.showHomeMenu();
                } else if (!this.cooking) {
                    // if the player isn't in the main menu, even if it's his turn, he is not interrupted
                    this.menuOptions();
                    System.out.print("Your choice: ");
                }
            }
        }
        if (turn) {
            this.gamesLog.add("It's " + playerNickname + "'s turn");
        } else if (this.turnPlayed > 0) {
            this.gamesLog.add(playerNickname + " passed the turn");
        }
    }

    /**
     * METHOD TO SHOW TO THE CLIENT ALL POSSIBLE MOVES
     */
    @Override
    public void showHomeMenu() {
        this.menuOptions();
        this.choice = userIntegerInput("Your choice", (num) -> {
            if (num == null) {
                return false;
            }
            if (num < 1 || num > 13) {
                System.out.print("Invalid choice, enter a valid option. ");
                return false;
            }
            return true;
        });
        if (this.choice == -1) {
            return;
        }
        System.out.println("SELECTION: " + this.choice);
        this.menuChoice(this.choice);
    }

    /**
     * method used to let the user perform the desired action
     *
     * @param choice menu choice selected by the player
     */
    private void menuChoice(int choice) {
        try {
            switch (choice) {
                case 1: {
                    this.printer.showHand(this.hand);
                    this.showHomeMenu();
                    break;
                }
                case 2: {
                    this.placeCard();
                    break;
                }
                case 3: {
                    this.printer.showObjectiveCard("\n--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
                    this.showHomeMenu();
                    break;
                }
                case 4: {
                    this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", List.of(this.serialPrivateObjectiveCard));
                    this.showHomeMenu();
                    break;
                }
                case 5: {
                    this.playersBoard.get(this.nickname).printBoard();
                    this.showHomeMenu();
                    break;
                }
                case 6: {
                    this.cooking = true;
                    String playerChosen;
                    playerChosen = userStringInput("Choose player board to view: [" + String.join("], [", this.playersBoard.keySet()) + "]" + "\nPlayer",
                            (input) -> {
                                if (input == null || input.isBlank()) {
                                    return false;
                                }
                                if (!this.playersBoard.containsKey(input)) {
                                    System.out.println("Player " + input + " not found");
                                    return false;
                                }
                                return true;
                            });
                    if (playerChosen == null) {
                        return;
                    }
                    System.out.println("Player chosen: " + playerChosen);
                    this.playersBoard.get(playerChosen).printBoard();
                    System.out.println();
                    this.printer.collectedResource(this.playersCollectedResources.get(playerChosen));
                    this.cooking = false;
                    this.showHomeMenu();
                    break;
                }
                case 7: {
                    this.cooking = true;
                    this.sendMessage();
                    this.cooking = false;
                    this.showHomeMenu();
                    break;
                }
                case 8: {
                    this.printer.showHistory(this.gamesLog);
                    this.showHomeMenu();
                    break;
                }
                case 9: {
                    System.out.println("Player's position are: ");
                    this.playersPosition.forEach((key, value) -> System.out.println(key + ": " + value));
                    // test -> this.playerPositions.forEach((key, value) -> System.out.print(String.join("\n", key + ": " + value)));
                    this.showHomeMenu();
                    break;
                }
                case 10: {
                    this.drawCard();
                    break;
                }
                case 11: {
                    this.printer.showPlayersScore(this.playersScore);
                    this.showHomeMenu();
                    break;
                }
                case 12: {
                    this.cooking = true;
                    synchronized (this.chat) {
                        if (!chat.isEmpty()) {
                            String playerChosen;
                            playerChosen = userStringInput("Choose player to see the chat: [" + (String.join("], [", chat.keySet()) + "]") + "\nPlayer",
                                    (input) -> {
                                        if (input == null || input.isBlank()) {
                                            return false;
                                        }
                                        if (!(chat.containsKey(input) || input.equals("global"))) {
                                            System.out.print("Chat with " + input + " doesn't exist");
                                            return false;
                                        }
                                        return true;
                                    });
                            if (playerChosen == null) {
                                return;
                            }
                            synchronized (chat.get(playerChosen)) {
                                System.out.println("CHAT WITH " + playerChosen.toUpperCase() + "\n" + chat.get(playerChosen)
                                        .stream()
                                        .map(message -> message + ";\n")
                                        .collect(Collectors.joining()));
                            }
                        } else {
                            System.out.println("No message exists.");
                        }
                        this.newMessage = false;
                    }
                    this.cooking = false;
                    this.showHomeMenu();
                    break;
                }
            }
        } catch (GenericException e) {
            this.menuChoice(Integer.parseInt(e.getMessage()));
        }
        this.choice = 0;
    }

    private void menuOptions() {
        System.out.println("\n--- HOME MENU " + this.nickname.toUpperCase() + " ---");
        if (this.myTurn) {
            System.out.println("\u001B[32mIt's your turn\u001B[0m");
        } else {
            System.out.println("\u001B[31mIt's not your turn\u001B[0m");
        }
        System.out.println("\t[1] to view your hand");
        System.out.println("\t[2] to place a card (only when in turn)");
        System.out.println("\t[3] to view common objective card");
        System.out.println("\t[4] to view your private objective card");
        System.out.println("\t[5] to view your board");
        System.out.println("\t[6] to view other players' board");
        System.out.println("\t[7] to send a message in the chat");
        System.out.println("\t[8] to show game's history");
        System.out.println("\t[9] to show players' turns");
        System.out.println("\t[10] to draw card (only when in turn)");
        System.out.println("\t[11] to view players' score");
        System.out.println("\t[12] to view chat [" + (this.newMessage ? "!" : "no new messages") + "]");
    }

    private Integer userIntegerInput(String messageToPrint, Function<Integer, Boolean> validator) {
        Integer num = null;
        System.out.print(messageToPrint + ": ");
        do {

            try {
                String tmp = this.newReader.readInput();
                if (tmp != null) {
                    num = Integer.parseInt(tmp);
                }
            } catch (NumberFormatException e) {
                System.out.println("Error: Please input valid numbers.");
            }
        } while (connectionOpen && !validator.apply(num));
        if (!connectionOpen) {
            return -1;
        }
        return num;
    }

    private String userStringInput(String messageToPrint, Function<String, Boolean> validator) {
        String input;
        System.out.print(messageToPrint + ": ");
        do {
            input = this.newReader.readInput();
        } while (connectionOpen && !validator.apply(input));
        if (!connectionOpen) {
            return null;
        }
        return input;
    }

    /**
     * method used to register a message sent by any player in the game
     *
     * @param key     can be [global] or a player [nickname], it is used to map the chat
     * @param message message sent in chat by the player
     */
    private void registerChatMessage(String key, String message) {
        if (this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(message);
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(key, new LinkedList<>(Collections.singletonList(message)));
            }
        }
        this.newMessage = true;
    }

    @Override
    public void restartConnection(ServerInterface virtualServer, ConnectionBuilder connectionBuilder) {
        if (virtualServer == this.virtualServer) {
            int attemptCount = 0;       // after tot attempts ask to keep trying
            int sleepTime = 1000;       // initial delay
            int maxTime = 20000;        // caps the sleepTime
            int totalElapsedTime = 0;   // to be deleted
            double backOffBase = 1.05;  // changes the exponential growth of the time
            System.err.println("\nInternet Connection Lost, trying to reconnect...");
            int t = 0;
            while (t < 80000) {
                t++;
            }
            connectionOpen = false;
            while (!connectionOpen) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    if (this.gameName == null || this.nickname == null) {
                        System.err.println("GameName or PlayerName is Null");
                        System.out.println("This program will be terminated");
                        System.exit(1);
                    }
                    // WHEN CONNECTION CLIENT <-> SERVER IS RESTORED, THE VIEW RECEIVES THE NEW VIRTUAL SERVER
                    this.virtualServer = connectionBuilder.createServerConnection(virtualServer.getClientDispatcher());
                    //this.setVirtualServer(this.virtualServer);
                    connectionOpen = true;
                    virtualServer.setConnectionOpen(true);
                    System.out.println("\u001B[33mConnection restored, you can keep playing\u001B[0m");
                    this.reconnectToGame();
                } catch (IOException e) {
                    // exponential backoff algorithm
                    attemptCount++;
                    totalElapsedTime += sleepTime;
                    sleepTime = (int) Math.min(sleepTime * Math.pow(backOffBase, attemptCount), maxTime);
                    System.out.println("Attempt #" + attemptCount + "\tSleep Time: " + sleepTime + " ms\tElapsed Time: " + totalElapsedTime / 1000 + " s");
                    if (attemptCount > 10) {
                        //after some attempts wait for user input
                        String answer;
                        Scanner scanner = new Scanner(System.in);
                        System.out.println("Still waiting for Internet Connection, do you want to keep trying? (y/n)");
                        do {
                            answer = scanner.nextLine();
                            if (answer.equalsIgnoreCase("y")) {
                                sleepTime = 2000;
                                attemptCount = 0;
                                System.out.println("trying to reconnect...");
                            } else if (answer.equalsIgnoreCase("n")) {
                                System.exit(0);
                            } else {
                                System.out.println("Character not recognised, please choose (y/n)");
                            }
                        } while (!(answer.equals("y") || answer.equals("n")));
                    }
                }
            }
        } else {
            System.out.println("Virtual server passed already updated");
        }
    }

    @Override
    public void onClosingGame(String disconnectedPlayer) {
        System.err.println("\n///////////////////////////////////////////////////////////");
        if (this.nickname.equals(disconnectedPlayer)) {
            System.err.println("You seem to have lost connection, the game is being closed...");
        } else {
            System.err.println("[Player: " + disconnectedPlayer + "] has disconnected, the game is being closed...");
        }
        System.exit(0);
    }

}
