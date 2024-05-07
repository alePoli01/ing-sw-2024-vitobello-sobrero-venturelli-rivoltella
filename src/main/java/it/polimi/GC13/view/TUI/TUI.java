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
    private final ServerInterface virtualServer;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard = new LinkedList<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private int choice = 0;
    private boolean myTurn = false;
    private final List<String> gamesLog = new ArrayList<>();
    private final Map<String, Position> playerPositions = new HashMap<>();
    private String nickname;
    private final Printer printer = new Printer();
    private final ArrayList<Integer> goldCardsAvailable = new ArrayList<>();
    private final ArrayList<Integer> resourceCardAvailable = new ArrayList<>();
    private int turnPlayed = - 1;
    private final Map<String, BoardView> playersBoard = new LinkedHashMap<>();

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.checkForExistingGame();
    }

    /*
        used to update players hand in TUI
     */
    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if (playerNickname.equals(this.nickname)) {
            synchronized (this.hand){
                this.hand.clear();
                this.hand.addAll(availableCard);
            }
        } else {
            this.gamesLog.add(playerNickname + " has drawn a card");
        }
    }

    @Override
    public void updateGoldCardsAvailableToDraw(int[] goldCardSerial) {
        Arrays.stream(goldCardSerial)
                .forEach(this.goldCardsAvailable::add);
    }

    @Override
    public void updateResourceCardsAvailableToDraw(int[] resourceFacedUpSerial) {
        Arrays.stream(resourceFacedUpSerial)
                .forEach(this.resourceCardAvailable::add);
    }

    @Override
    public void checkForExistingGame() {
        this.virtualServer.checkForExistingGame();
        System.out.println("++Sent: checkForExistingGame");
    }

    /*
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
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game: [" + string + "] --|players in waiting room: " + numCurrPlayer +"|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = this.reader.readLine();
        } while (!gameNameWaitingPlayersMap.containsKey(gameName));

        //massage is ready to be sent
        this.virtualServer.addPlayerToGame(this.nickname, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    /*
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

    /*
        SETUP PHASE methods to the player
        startCardSetupPhase to chose which side to place your card
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
            System.out.println(message + ".\nWaiting for other players...");
            if (this.turnPlayed >= 0) {
                System.out.println("Remaining cards in hand are: " + this.hand.stream().map(Object::toString).collect(Collectors.joining(" ")));
                this.printer.comeBack(this);
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
        this.printer.showObjectiveCard("--- COMMON OBJECTIVE CARDS ---", serialCommonObjectiveCard);
    }

    /*
        METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", privateObjectiveCards);
            try {
                while (!privateObjectiveCards.contains(choice)) {
                    System.out.print("Choose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                }
                this.virtualServer.choosePrivateObjectiveCard(choice);
                this.choice = 0;
                System.out.println("++Sent private objective card choice message");
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
        this.printer.showDrawableCards(this.goldCardsAvailable, this.resourceCardAvailable);
        if (this.myTurn && this.hand.size() == 2) {
            try {
                int deck;
                do {
                    System.out.print("Choose the deck from where to withdraw the card.\n[1] GOLD [2] RESOURCE: ");
                    deck = Integer.parseInt(reader.readLine());
                } while (deck < 1 || deck > 2);
                do {
                    if (deck == 1) {
                        System.out.print("Available serial numbers from GOLD DECK are:");
                        this.goldCardsAvailable.forEach(serial -> System.out.print(" " + serial));
                    } else {
                        System.out.print("Available serial numbers from RESOURCE DECK are:");
                        this.resourceCardAvailable.forEach(serial -> System.out.print(" " + serial));
                    }
                    System.out.print("\nYour choice: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                    this.virtualServer.drawCard(deck, choice);
                } while (deck == 1 ? this.goldCardsAvailable.contains(this.choice) : this.resourceCardAvailable.contains(this.choice));
                this.choice = 0;
                this.turnPlayed++;
                System.out.println("You have passed the turn");
                this.showHomeMenu();
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
        } else {
            System.out.println("You cannot draw from the deck if it is not your turn or you didn't place one card on the board.");
            this.printer.comeBack(this);
        }
    }

    /*
        METHOD TO SHOW TO THE CLIENT ALL POSSIBLE MOVES
     */
    @Override
    public void showHomeMenu() {
        System.out.println("\n--- HOME MENU ---");
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
        System.out.print("Your choice: ");
        try {
            this.choice = Integer.parseInt(reader.readLine());
            System.out.println("SELECTION: " + this.choice);
        } catch (IOException e) {
            System.out.print("Error: Please put a number: ");
        }
        switch (this.choice) {
            case 1: {
                this.printer.showHand(this.hand);
                break;
            }
            case 2: {
                this.placeCard();
                break;
            }
            case 3: {
                this.printer.showObjectiveCard("\n--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
                break;
            }
            case 4: {
                this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", List.of(this.serialPrivateObjectiveCard));
                break;
            }
            case 5: {
                this.playersBoard.get(this.nickname).printBoard();
                this.printer.comeBack(this);
                break;
            }
            case 6: {
                try {
                    String playerChosen;
                    do {
                        System.out.println("Choose player board to view: [" + String.join("], [", this.playersBoard.keySet()) + "]");
                        System.out.print("Your choice: ");
                        playerChosen = reader.readLine();
                        while (!this.playersBoard.containsKey(playerChosen)) {
                            System.out.println("Player " + playerChosen + " not found");
                        }
                        System.out.println("Player chosen:" + playerChosen);
                    } while (!this.playersBoard.containsKey(playerChosen));
                    this.playersBoard.get(playerChosen).printBoard();
                    this.printer.comeBack(this);
                } catch (IOException e) {
                    System.out.println("Error: Please put a number");
                }
                break;
            }
            case 7: {
                System.out.println("To do");
                break;
            }
            case 8: {
                this.printer.showHistory(this.gamesLog);
                break;
            }
            case 9: {
                System.out.println("Player's position are: ");
                this.playerPositions.forEach((key, value) -> System.out.println(key + ": " + value));
                // test -> this.playerPositions.forEach((key, value) -> System.out.print(String.join("\n", key + ": " + value)));
                break;
            }
            case 10:
                this.drawCard();
                break;
        }
        this.printer.comeBack(this);
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
    public void displayAvailableCells(Set<Coordinates> availableCells) {
        System.out.println("Available cells are: ");
        System.out.println(availableCells);
        this.printer.comeBack(this);
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

    /*
        NOTIFY RESPECTIVE CLIENT WHEN IT'S THEIR TURN
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
        }
        if (turn) {
            this.gamesLog.add("\nIt's " + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
        if (this.turnPlayed == 0) {
            this.showHomeMenu();
        }
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
                    System.out.println("Choose serial card or press [0] to go back to HOME MENU: ");
                    serialCardToPlace = scanner.nextInt();
                    if (serialCardToPlace == 0) {
                        this.printer.comeBack(this);
                    }
                }
                X = (scanner.nextInt());
                Y = (scanner.nextInt());
                isFlipped = scanner.nextInt() == 1;
            } catch (InputMismatchException e) {
                System.out.print("Error: Please input valid numbers.");
            }
            this.virtualServer.placeCard(serialCardToPlace, isFlipped, X, Y);
        } else if (!this.myTurn) {
            System.out.println("It's not your turn");
        } else {
            System.out.println("You have already placed a card. You need to draw a card to pass the turn.");
        }
    }
}
