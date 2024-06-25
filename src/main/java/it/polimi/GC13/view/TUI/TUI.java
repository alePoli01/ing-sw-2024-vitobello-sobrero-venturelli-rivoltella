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
import it.polimi.GC13.view.GUI.game.ChatMessage;
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
    private final Map<String, List<ChatMessage>> chat = new HashMap<>();
    private boolean newMessage = false;
    private boolean connectionOpen = true;
    private boolean interrupt = false;

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
            try {
                this.choice = userIntegerInput("There are existing games\n\t[1] to create a new Game\n\t[2] to join an existing Game\nYour choice", num -> !rangeVerifier(num, 1, 2), "Please enter either [1] or [2]\nYour choice: ");
            } catch (GenericException e) {
                System.out.println(e.getMessage());
                return;
            }
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
        try {
            this.nickname = userStringInput("Choose your nickname", input -> !stringVerifier(input), "");
            gameName = userStringInput("Choose a name for the new Game", input -> !stringVerifier(input), "");
            playersNumber = userIntegerInput("Choose number of players in the game [min 2, max 4]", num -> !rangeVerifier(num, 2, 4), "Please enter either [2], [3] or [4].");
        } catch (GenericException e) {
            System.out.print(e.getMessage());
            return;
        }
        this.virtualServer.sendMessageFromClient(new CreateNewGameMessage(this.nickname, playersNumber, gameName));
    }

    private void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) {
        String gameName;

        try {
            this.nickname = userStringInput("Choose your nickname", input -> !stringVerifier(input), "");
            System.out.println("Joinable Games:");
            gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>gameName: [" + string + "] --|players in waiting room: " + numCurrPlayer + "|"));
            gameName = userStringInput("Select the game to join using its name", input -> !stringVerifier(input, gameNameWaitingPlayersMap.keySet().stream().toList()), "Game name not found.");
            //massage is ready to be sent
            this.virtualServer.sendMessageFromClient(new AddPlayerToGameMessage(this.nickname, gameName));
        } catch (GenericException e) {
            System.out.print(e.getMessage());
        }
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
            try {
                String tokenColorChosen = userStringInput("Choose your token color " + joiner + "\nColor", input -> !stringVerifier(input, tokenColorList.stream().map(TokenColor::toString).collect(Collectors.toList())), "Token color not found.");
                this.virtualServer.sendMessageFromClient(new TokenChoiceMessage(TokenColor.valueOf(tokenColorChosen)));
            } catch (GenericException e) {
                System.out.print(e.getMessage());
            }
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

            try {
                this.choice = userIntegerInput("Choose which side you would like to place your start card:\n\t[1] FRONT\n\t[2] BACK\nYour choice", num -> !rangeVerifier(num, 1, 2), "Please enter either [1] or [2].");
            } catch (GenericException e) {
                System.out.print(e.getMessage());
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

            try {
                this.choice = userIntegerInput("\nChoose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]", num -> !intVerifier(num, privateObjectiveCards), "You don't have the selected card.");
            } catch (GenericException e) {
                System.out.print(e.getMessage());
                return;
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
    public void drawCard() {
        System.out.println("\n--- DRAWABLE CARDS ---");
        System.out.println("--- Gold Deck ---");
        this.printer.showDrawableCards(this.goldCardsAvailable);
        System.out.println("\n--- Resource Deck ---");
        this.printer.showDrawableCards(this.resourceCardsAvailable);

        if (this.myTurn && this.hand.size() == 2) {
            try {
                this.choice = userIntegerInput("Choose the card to withdraw", num -> !(intVerifier(num, this.goldCardsAvailable.keySet().stream().toList()) && intVerifier(num, this.resourceCardsAvailable.keySet().stream().toList())), "Selected card isn't available.");
            } catch (GenericException e) {
                System.out.print(e.getMessage());
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
        System.out.println("\n\n" + playerNickname.toUpperCase() + " has reached 20 points. There will be another turn for all players after the current turn ends.");
        if (lastTurnSetterPosition < this.playersPosition.size()) {
            System.out.println("Players to finish the turn: " + this.playersPosition.entrySet().stream().filter(entry -> entry.getValue().getIntPosition() > lastTurnSetterPosition).map(Map.Entry::getKey).collect(Collectors.joining(" ")));
        }

        this.interrupt = true;
        this.showHomeMenu();
    }

    /**
     * METHOD USED TO PLACE CARD ON THE BOARD
     */
    @Override
    public void placeCard() {
        if (this.myTurn && this.hand.size() == 3) {
            int X, Y, serialCardToPlace;
            this.playersBoard.get(this.nickname).printBoard();
            System.out.println();
            this.printer.collectedResource(this.playersCollectedResources.get(this.nickname));
            this.printer.showHand(this.hand);

            try {
                serialCardToPlace = userIntegerInput("Enter serial card", num -> !intVerifier(num, this.hand), "You don't have the selected card. Available cards are:");
                X = userIntegerInput("Enter X coordinate", num -> !rangeVerifier(num, 0), "Invalid coordinate, enter X > 0.");
                Y = userIntegerInput("Enter Y coordinate", num -> !rangeVerifier(num, 0), "Invalid coordinate, enter Y > 0.");
                this.choice = userIntegerInput("Enter\n\t[1] for FRONT\n\t[2] for BACK\nYour choice", num -> !rangeVerifier(num, 1, 2), "Please enter either [1] or [2].");
            } catch (GenericException e) {
                System.out.print(e.getMessage());
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
        this.cooking = true;
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
    private void sendMessage() {
        String playerChosen;
        String message;
        try {
            playerChosen = userStringInput("Choose who to send the message to: [" + (String.join("], [", this.playersBoard.keySet()) + "]") + " or [global]\nPlayer", input -> !stringVerifier(input, addToSet(this.playersBoard.keySet(), List.of("global"))), "playerName not found");

            message = userStringInput("Write down your message", input -> !stringVerifier(input), "");
            this.virtualServer.sendMessageFromClient(new NewMessage(this.nickname, playerChosen, message));
        } catch (GenericException e) {
            System.out.print(e.getMessage());
            return;
        }
    }

    private List<String> addToSet(Set<String> set, List<String> list) {
        List<String> newList = new ArrayList<>(set.stream().toList());
        newList.addAll(list);
        return newList;
    }

    /**
     * NOTIFY RESPECTIVE CLIENT WHEN IT'S THEIR TURN
     * OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
            // prints for the first time the main menu for
            if (this.turnPlayed == 0) {
                this.showHomeMenu();
            } else {
                if (!this.myTurn) {
                    // notifies the player has passed the turn
                    System.out.println("You have passed the turn");
                    this.showHomeMenu();
                } else if (!this.cooking) {
                    // if the player isn't in the main menu, even if it's his turn, he is not interrupted
                    this.interrupt = true;
                    this.showHomeMenu();
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
        try {
            this.interrupt = false;
            this.choice = userIntegerInput("Your choice", num -> !rangeVerifier(num, 1, 12), "Invalid choice, enter a valid option.");
        } catch (GenericException e) {
            System.out.print(e.getMessage());
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
                try {
                    playerChosen = userStringInput("Choose player board to view: [" + String.join("], [", this.playersBoard.keySet()) + "]" + "\nPlayer", input -> !stringVerifier(input, this.playersBoard.keySet().stream().toList()), "");
                } catch (GenericException e) {
                    System.out.print(e.getMessage());
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
                        try {
                            playerChosen = userStringInput("Choose player to see the chat: [" + (String.join("], [", chat.keySet()) + "]") + "\nPlayer",
                                    input -> !stringVerifier(input, addToSet(this.chat.keySet(), List.of("global"))), "No player found.");
                        } catch (GenericException e) {
                            System.out.print(e.getMessage());
                            return;
                        }

                        synchronized (chat.get(playerChosen)) {
                            System.out.println("CHAT WITH " + playerChosen.toUpperCase() + "\n" + chat.get(playerChosen)
                                    .stream()
                                    .map(chatMessage -> chatMessage.sender() + ": " + chatMessage.content() + ";\n")
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
        this.choice = 0;
    }

    /**
        MENU OPTIONS
     */
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

    /**
     * rangeVerifier is used to verify input from user. numToVerify has to be higher than value to be verified
     * @param numToVerify user's input
     * @param value has to be lower than numToVerify in order to accept the input
     * @return false when input correct
     */
    private boolean rangeVerifier(int numToVerify, int value) {
        return numToVerify <= value;
    }

    /**
     * rangeVerifier is used to verify input from user. numToVerify has to be higher than highValue or lower than lowValue to be verified
     * @param numToVerify user's input
     * @param lowValue low range value
     * @param highValue high range value
     * @return false when input correct
     */
    private boolean rangeVerifier(int numToVerify, int lowValue, int highValue) {
        return numToVerify < lowValue || numToVerify > highValue;
    }

    private boolean intVerifier(int numToVerify, List<Integer> value) {
        return value.stream().noneMatch(v -> v == numToVerify);
    }

    private boolean stringVerifier(String input) {
        return input == null || input.isBlank();
    }

    private boolean stringVerifier(String input, List<String> possibleChoices) {
        if (input == null) {
            return true;
        }
        return possibleChoices.stream().noneMatch(input::equals);
    }

    private Integer userIntegerInput(String messageToPrint, Function<Integer, Boolean> validator, String errorMessage) throws GenericException {
        int num = 0;
        System.out.print(messageToPrint + ": ");

        do {
            try {
                String tmp = this.newReader.readInput();
                if (tmp != null) {
                    num = Integer.parseInt(tmp);
                    if (!validator.apply(num)) System.out.print(errorMessage + "\n" + messageToPrint + ": ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error: Please input a valid number.\n" + messageToPrint + ": ");
            }

            if (!this.connectionOpen) {
                throw new GenericException("Connection Lost\n");
            } else if (this.interrupt) {
                throw new GenericException("");
            }
        } while (!validator.apply(num));

        return num;
    }

    private String userStringInput(String messageToPrint, Function<String, Boolean> validator, String errorMessage) throws GenericException {
        String input;
        System.out.print(messageToPrint + ": ");

        do {
            input = this.newReader.readInput();
            if (input != null) {
                if (!validator.apply(input)) System.out.print(errorMessage + "\n" + messageToPrint + ": ");
            }
            if (!this.connectionOpen) {
                throw new GenericException("Connection lost\n");
            } else if (this.interrupt) {
                throw new GenericException("");
            }
        } while (!validator.apply(input));

        return input;
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
        // CASE A : I AM THE SENDER
        if (this.nickname.equals(sender)) {
            registerMessage(recipient, sender, message);
        } else if (recipient.equals("global")) {
            // CASE B : global chat
            registerMessage(recipient, sender, message);
        } else {
            // CASE C : I AM THE ONLY RECEIVER
            registerMessage(sender, sender, message);
        }
    }

    private void registerMessage(String chatName, String sender, String message) {
        if (this.chat.containsKey(chatName)) {
            synchronized (this.chat.get(chatName)) {
                this.chat.get(chatName).add(new ChatMessage(sender, message));
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(chatName, new LinkedList<>(Collections.singletonList(new ChatMessage(sender, message))));
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
            this.connectionOpen = false;
            while (!this.connectionOpen) {
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
                    this.connectionOpen = true;
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
