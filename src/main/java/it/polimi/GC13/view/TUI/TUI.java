package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;

public class TUI implements View {
    private ServerInterface virtualServer;
    private String nickname;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard = new LinkedList<>();
    private boolean myTurn = false;
    private int turnPlayed = - 1;
    private final Map<String, Integer> playersScore = new HashMap<>();
    private final Map<String, Position> playerPositions = new HashMap<>();
    private final Map<Integer, Boolean> goldCardsAvailable = new HashMap<>();
    private final Map<Integer, Boolean> resourceCardsAvailable = new HashMap<>();
    private final Map<String, BoardView> playersBoard = new LinkedHashMap<>();
    private final List<String> gamesLog = new ArrayList<>();
    private boolean cooking = false;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private int choice = 0;
    private final Printer printer = new Printer();
    private final Map<String, List<String>> chat = new HashMap<>();
    private boolean newMessage = false;

    public TUI() {
        this.virtualServer = null;
    }

    @Override
    public void setVirtualServer(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
    }

    @Override
    public void startView() {
        this.checkForExistingGame();
    }

    /**
        used to update players hand in TUI
     */
    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if (playerNickname.equals(this.nickname)) {
            synchronized (this.hand) {
                this.hand.clear();
                this.hand.addAll(availableCard);
            }
        } else {
            this.gamesLog.add(playerNickname + " has drawn a card");
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
        this.virtualServer.checkForExistingGame();
        //System.out.println("++Sent: checkForExistingGame");
    }

    /**
        JOINING PHASE
        [1] create new game
        [2] join an existing one
     */
    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) {
        if (gameNameWaitingPlayersMap.isEmpty()) {
            System.out.println("There is no existing game");
            try {
                createNewGame();
            } catch (IOException e) {
                System.out.println("Error while creating the game.");
            }
        } else {
            //ask what the player wants to do
            System.out.println("There are existing games, choose:\n\t[1] to create a new Game\n\t[2] to join an existing Game");
            do {
                try {
                    this.choice = Integer.parseInt(this.reader.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Error: Please put a number");
                }
            } while (this.choice < 1 || this.choice > 2);

            if (this.choice == 1) {
                //player wants to create a new game
                try {
                    this.createNewGame();
                } catch (IOException e) {
                    System.out.println("Error while creating the game.");
                }
            } else {
                //player can and wants to join an existing game
                try {
                    joinExistingGame(gameNameWaitingPlayersMap);
                } catch (IOException e) {
                    System.out.println("Error while joining game.");
                }
            }
            this.choice = 0;
        }

    }

    private void createNewGame() throws IOException {
        /*
            create and send message for a new game
         */
        String gameName;
        int playersNumber = -1;

        //asking for all the contents of the message
        System.out.print("Choose your nickname: ");
        this.nickname = this.reader.readLine();

        System.out.print("Choose a name for the new Game: ");
        gameName = this.reader.readLine();

        System.out.print("Choose Number of players in the game [min 2, max 4]: ");
        do {
            try {
                playersNumber = Integer.parseInt(this.reader.readLine());
                if (playersNumber < 2 || playersNumber > 4) {
                    System.out.print("Error: Please choose a number between 2 and 4: ");
                }
            } catch (NumberFormatException e) {
                System.out.print("Error: Please put a number: ");
            }
        } while (playersNumber < 2 || playersNumber > 4);

        //massage is ready to be sent
        virtualServer.createNewGame(this.nickname, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    private void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) throws IOException {
        String gameName;

        System.out.print("Choose your nickname: ");
        this.nickname = this.reader.readLine();

        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game: [" + string + "] --|players in waiting room: " + numCurrPlayer + "|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = this.reader.readLine();
        } while (!gameNameWaitingPlayersMap.containsKey(gameName));

        //massage is ready to be sent
        this.virtualServer.addPlayerToGame(this.nickname, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    /**
        SETUP PHASE
        token choice when all players joined the game
        waiting when readPlayers < neededPlayers
     */
    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList) {
        String tokenColorChosen;
        boolean flag = false;
        StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");

        if (readyPlayers == neededPlayers) {
            tokenColorList.stream().map(TokenColor::toString).forEach(joiner::add);
            System.out.println("\n--- SETUP PHASE [1/2]---");
            System.out.println("Choose your token color: " + joiner);
            do {
                try {
                    tokenColorChosen = this.reader.readLine().toUpperCase();
                } catch (IOException e) {
                    System.out.println("error in reading input");
                    tokenColorChosen = null;
                }
                String finalTokenColor = tokenColorChosen; //per debugging
                if (tokenColorList.stream().anyMatch(tc -> tc.name().equalsIgnoreCase(finalTokenColor))) {
                    flag = true;
                    // calls the controller to update the model
                    this.virtualServer.chooseToken(TokenColor.valueOf(tokenColorChosen));
                } else {
                    System.out.println("Color not valid, you can chose: " + joiner);
                }
            } while (!flag);
        } else {
            System.out.println("--|players in waiting room: " + readyPlayers + "/" + neededPlayers);
        }
    }

    /**
        SETUP PHASE methods to the player
        startCardSetupPhase to chose which side to place your start card
     */
    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) {
        if (playerNickname.equals(this.nickname)) {
            System.out.println("You choose " + tokenColor + " token\n");
            System.out.println("--- SETUP PHASE [2/2] ---");
            System.out.println("--- START CARD ---");
            this.printer.showHand(this.hand);
            System.out.println("Choose which side you would like to place your start card:\n\t[1] FRONT\n\t[2] BACK");
            do {
                try {
                    this.choice = Integer.parseInt(this.reader.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Error: Please put a number");
                }
            } while (this.choice < 1 || this.choice > 2);

            this.virtualServer.placeStartCard(this.choice != 1);
            this.choice = 0;
        }
        this.gamesLog.add(playerNickname + " choose " + tokenColor + " token");
    }

    /*
        NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn) {
        String message = playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn;
        if (playerNickname.equals(this.nickname)) {
            if (this.turnPlayed >= 0) {
                System.out.println(message);
                this.showHomeMenu(); // show menu after placing a card
            } else {
                System.out.println(message + ".\nWaiting for other players...");
            }
        }
        if (!this.playerPositions.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add(message);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);
    }

    /*
        NOTIFY THE CLIENTS ABOUT THE COMMON OBJECTIVE CARD
    */
    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }

    /*
        METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            this.printer.showObjectiveCard("--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
            this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", privateObjectiveCards);
            try {
                while (!privateObjectiveCards.contains(choice)) {
                    System.out.print("Choose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                }
                this.virtualServer.choosePrivateObjectiveCard(choice);
                this.choice = 0;
                //System.out.println("++Sent private objective card choice message");
            } catch (NumberFormatException | IOException e) {
                System.out.print("Error: Please put a number.\nChoose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]: ");
            }
        }
    }

    /*
        NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
    @Override
    public void setPrivateObjectiveCard(String playerNickname, int serialPrivateObjectiveCard, int readyPlayers, int neededPlayers) {
        if (playerNickname.equals(this.nickname)) {
            this.turnPlayed++;
            this.serialPrivateObjectiveCard = serialPrivateObjectiveCard;
            String message = "Your private objective card is " + serialPrivateObjectiveCard;
            System.out.println(message + ".");
            this.gamesLog.add(message);
        } else {
            this.gamesLog.add(playerNickname + " choose private objective card");
        }
        if (this.serialPrivateObjectiveCard != 0 && readyPlayers != neededPlayers) {
            System.out.println("--|players that chose objective card: " + readyPlayers + "/" + neededPlayers);
        }
    }

    /*
        METHOD TO REQUEST DRAW CARD
     */
    @Override
    public void drawCard() {
        this.printer.showDrawableCards(this.goldCardsAvailable, this.resourceCardsAvailable);
        if (this.myTurn && this.hand.size() == 2) {
            try {
                do {
                    System.out.print("Choose the card to withdraw: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                } while (!this.goldCardsAvailable.containsKey(this.choice) && !this.resourceCardsAvailable.containsKey(this.choice));
                this.virtualServer.drawCard(choice);
                System.out.println("+++ Sent draw card");
                this.choice = 0;
                this.turnPlayed++;
                System.out.println("You have passed the turn");
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
        } else {
            System.out.println("You cannot draw from the deck if it is not your turn or you didn't place one card on the board.");
            this.showHomeMenu();
        }
    }

    /*
        METHOD TO SHOW TO THE CLIENT ALL POSSIBLE MOVES
     */
    @Override
    public void showHomeMenu() {
        this.menuOption();
        try {
            do {
                this.choice = Integer.parseInt(reader.readLine());
                System.out.println("SELECTION: " + this.choice);
                if (this.choice < 1 || this.choice > 13) {
                    System.out.print("Invalid choice. Enter a valid option: ");
                }
            } while (this.choice < 1 || this.choice > 13);
        } catch (IOException e) {
            // Exception handling for IOException
            System.out.print("Error: Please put a number: ");
        }

        switch (this.choice) {
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
                try {
                    this.cooking = true;
                    String playerChosen;
                    do {
                        System.out.println("Choose player board to view: [" + String.join("], [", this.playersBoard.keySet()) + "]");
                        System.out.print("Player: ");
                        playerChosen = reader.readLine();
                        while (!this.playersBoard.containsKey(playerChosen)) {
                            System.out.print("Player " + playerChosen + " not found.\nEnter an existing player: ");
                            playerChosen = reader.readLine();
                        }
                    } while (!this.playersBoard.containsKey(playerChosen));
                    System.out.println("Player chosen: " + playerChosen);
                    this.playersBoard.get(playerChosen).printBoard();
                } catch (IOException e) {
                    System.err.println("Error parsing the name");
                }
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
                this.playerPositions.forEach((key, value) -> System.out.println(key + ": " + value));
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
                    this.printer.seeChat(this.chat);
                    this.newMessage = false;
                }
                this.cooking = false;
                this.showHomeMenu();
                break;
            }
        }
        this.choice = 0;
    }

    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            System.out.println("Launching an exception");
            System.out.println(onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
        }
        this.gamesLog.add(onInputExceptionMessage.getErrorMessage());
    }

    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {
        String cellCoordinates = availableCells.stream()
                .map(cell -> "(" + cell.getX() + ", " + cell.getY() + ")")
                .collect(Collectors.joining("\n"));
        System.out.println("Available cells: " + cellCoordinates + ".");
        this.showHomeMenu();
    }


    @Override
    public void connectionLost() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    /*
        METHOD USED TO GIVE EACH USER VISIBILITY OF PLAYERS ORDER
     */
    @Override
    public void setPlayersOrder(Map<String, Position> playerPositions) {
        this.playerPositions.putAll(playerPositions);
    }

    @Override
    public void onSetLastTurn(String playerNickname, Position playerPosition) {
        System.out.print(nickname + " has reached 20 points. The will be another turn for players in position: ");
        for (; playerPosition.ordinal() < this.playerPositions.size(); playerPosition.next(this.playerPositions.size())) {
            System.out.print(playerPosition + " ");
            System.out.println(" and another one bonus.");
        }
    }

    /*
        METHOD USED TO PLACE CARD ON THE BOARD
     */
    @Override
    public void placeCard() {
        if (this.myTurn && this.hand.size() == 3) {
            int X = 0;
            int Y = 0;
            int serialCardToPlace = 0;
            boolean isFlipped = false;
            this.printer.showHand(this.hand);
            System.out.println("Enter serial card, X coordinate, Y coordinate, and [1] for FRONT [2] for BACK");
            System.out.println("example: " + this.hand.getFirst() + " (for serial card), 51 (for X), 51 (for Y), 1 (for side)");
            Scanner scanner = new Scanner(System.in);
            try {
                serialCardToPlace = scanner.nextInt();
                while (!this.hand.contains(serialCardToPlace)) {
                    System.out.println("You don't have the selected card. Available are: ");
                    this.hand.forEach(System.out::print);
                    serialCardToPlace = scanner.nextInt();
                }
                X = (scanner.nextInt());
                Y = (scanner.nextInt());
                isFlipped = scanner.nextInt() == 2;
            } catch (InputMismatchException e) {
                System.out.print("Error: Please input valid numbers.");
            }
            this.virtualServer.placeCard(serialCardToPlace, isFlipped, X, Y);
        } else if (!this.myTurn) {
            System.out.println("It's not your turn");
            this.showHomeMenu();
        } else {
            System.out.println("You have already placed a card. You need to draw a card to pass the turn.");
            this.showHomeMenu();
        }
    }

    public void updatePlayerScore(String playerNickname, int newPlayerScore) {
        this.playersScore.computeIfPresent(playerNickname, (key, oldValue) -> newPlayerScore);
        this.playersScore.putIfAbsent(playerNickname, newPlayerScore);
    }

    /**
     * method to save new a message in the chat
     * @param sender message sender
     * @param recipient message recipient
     * @param message string that contains the message itself
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

    private void registerChatMessage(String key, String message) {
        if (this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(message);
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(key, List.of(message));
            }
        }
        this.newMessage = true;
    }

    /**
     * method to send a message to any player or to everyone
     */
    private void sendMessage() {
        try {
            this.cooking = true;
            String playerChosen;
            String message;
            do {
                System.out.println("Choose who to send the message to: [" + (String.join("], [", this.playersBoard.keySet()) + "]") + " or [global]");
                System.out.print("Player: ");
                playerChosen = reader.readLine();
                while (!(this.playersBoard.containsKey(playerChosen) || playerChosen.equals("global"))) {
                    System.out.print("Player " + playerChosen + " doesn't exist.\nEnter an existing player: ");
                    playerChosen = reader.readLine();
                }
                System.out.print("Write down your message: ");
                message = reader.readLine();
            } while (!(this.playersBoard.containsKey(playerChosen) || playerChosen.equals("global")));
            this.virtualServer.writeMessage(this.nickname, playerChosen, message);
        } catch (IOException e) {
            System.err.println("Error parsing the name");
        }
    }

    /*
        NOTIFY RESPECTIVE CLIENT WHEN IT'S THEIR TURN
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
            // this.turnPlayed == 0 after private card is chosen
            if (this.myTurn && this.turnPlayed >= 0 && !cooking) {
                if (this.playerPositions.get(this.nickname).equals(Position.FIRST) && this.turnPlayed == 0) {
                    this.showHomeMenu();
                } else {
                    this.menuOption();
                }
            } else if (!this.myTurn) { // at the end of the turn, the player will see the MAIN MENU
                this.showHomeMenu();
            }
        }
        if (turn) {
            this.gamesLog.add("\nIt's " + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
    }

    private void menuOption() {
        System.out.println("\n--- HOME MENU " + this.nickname.toUpperCase() + "---");
        if (this.myTurn) {
            System.out.println("It's your turn");
        } else {
            System.out.println("It's not your turn");
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
        System.out.print("Your choice: ");
    }
}
