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

public class TUI implements View {
    private final ServerInterface virtualServer;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private int[] serialCommonObjectiveCard;
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private int choice = 0;
    private boolean turn = false;
    private final List<String> gamesLog = new ArrayList<>();
    private final Map<String, Position> playerPositions = new HashMap<>();
    private String nickname;
    private final Printer printer = new Printer();
    private final ArrayList<Integer> goldCardsAvailable = new ArrayList<>();
    private final ArrayList<Integer> resourceCardAvailable = new ArrayList<>();
    private int turnPlayed = 0;

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.checkForExistingGame();
    }

    /*
        used to update players hand in TUI
     */
    @Override
    public void handUpdate(String playerNickname, int[] availableCard) {
        if (playerNickname.equals(this.nickname)) {
            this.hand.clear();
            Arrays.stream(availableCard).forEach(this.hand::add);
        }
    }

    @Override
    public void updateGoldCardsAvailable(int[] goldCardSerial) {
        System.out.println("Updating Gold Cards Available");
        for (int i = 0; i < this.goldCardsAvailable.size(); i++) {
            this.goldCardsAvailable.add(i, goldCardSerial[i]);
        }
        this.goldCardsAvailable.forEach(serialCard -> System.out.println("Gold Card: " + serialCard));
    }

    @Override
    public void updateResourceCardsAvailable(int[] resourceFacedUpSerial) {
        System.out.println("Updating Resource Cards Available");
        for (int i = 0; i < this.resourceCardAvailable.size(); i++) {
            this.resourceCardAvailable.add(i, resourceFacedUpSerial[i]);
        }
        this.resourceCardAvailable.forEach(serialCard -> System.out.println("Resource Card: " + serialCard));
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
                    createNewGame();
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
            } catch (NumberFormatException e) {
                System.out.print("Error: Please put a number: ");
            }
            if (playersNumber < 2 || playersNumber > 4) {
                System.out.print("Error: Please choose a number between 2 and 4: ");
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
        SETUP PHASE methods to the player
        tokenSetupPhase to chose your token
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
            System.out.println("\n--- SETUP PHASE [2/2] ---");
            System.out.println("\n--- START CARD ---\n");
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
        } else {
            this.gamesLog.add(playerNickname + " choose " + tokenColor + " token");
        }

    }

    /*
        NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void onPlacedCard(String playerNickname, int cardPlaced, boolean isFlipped) {
        String message = playerNickname + " positioned " + cardPlaced + " on " + (isFlipped ? "back" : "front");
        if (playerNickname.equals(this.nickname)) {
            System.out.println(message);
        } else {
            this.gamesLog.add(message);
        }
    }

    /*
        NOTIFY THE CLIENTS ABOUT THE COMMON OBJECTIVE CARD
    */
    @Override
    public void setSerialCommonObjectiveCard(int[] serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
        this.printer.showObjectiveCard("--- COMMON OBJECTIVE CARDS ---", serialCommonObjectiveCard);
    }

    /*
        METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void chosePrivateObjectiveCard(String playerNickname, int[] privateObjectiveCard) {
        if (playerNickname.equals(this.nickname)) {
            this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", privateObjectiveCard);
            try {
                while (choice != 1 && choice != 2) {
                    System.out.print("Chose your private objective card [1] or [2]: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                }
                this.virtualServer.chosePrivateObjectiveCard(choice);
                this.choice = 0;
                System.out.println("++Sent private objective card choice message");
            } catch (NumberFormatException | IOException e) {
                System.out.print("Error: Please put a number.\nChose your private objective card [1] or [2]: ");
            }
        }
    }

    /*
        NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
    @Override
    public void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers) {
        if (readyPlayers == neededPlayers) {
            if (playerNickname.equals(this.nickname)) {
                this.serialPrivateObjectiveCard = indexPrivateObjectiveCard;
                System.out.println("Your private objective card is " + indexPrivateObjectiveCard);
            }
        } else if (this.serialPrivateObjectiveCard != 0) {
            System.out.println("--|players that chose objective card: " + readyPlayers + "/" + neededPlayers);
        }
    }

    /*
        METHOD TO REQUEST DRAW CARD
     */
    @Override
    public void drawCard() {
        this.printer.showDrawableCards(this.goldCardsAvailable, this.resourceCardAvailable);
        if (this.turn && this.hand.size() == 2) {
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
        if (this.turn) {
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
                this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", this.serialPrivateObjectiveCard);
                break;
            }
            case 5: {
                System.out.println("Nothing yet");
                break;
            }
            case 6: {
                System.out.println("To implement");
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
                System.out.println("Player's position are:");
                this.playerPositions.forEach((p, position) -> System.out.println(p + position));
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
        } else {
            this.gamesLog.add(onInputExceptionMessage.getErrorMessage());
        }
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
            this.turn = turn;
        } else if (turn) {
            this.gamesLog.add("\nIt's" + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
        if (this.turnPlayed == 0) {
            this.showHomeMenu();
        }
    }

    /*
        METHOD USED TO PLACE CARD ON THE BOARD
     */
    @Override
    public void placeCard() {
        if (this.turn) {
            int X = 0;
            int Y = 0;
            int cardToPlaceHandIndex = 0;
            boolean isFlipped = false;
            this.printer.showHand(this.hand);
            System.out.println("Enter hand index [1 -> 3], X coordinate, Y coordinate, and [1] for FRONT [2] for BACK");
            System.out.println("example: 1 (for index), 51 (for X), 51 (for Y), 1 (for side)");
            Scanner scanner = new Scanner(System.in);
            try {
                cardToPlaceHandIndex = scanner.nextInt();
                while (this.hand.get(cardToPlaceHandIndex) == null) {
                    System.out.println("You don't have a card in position:" + cardToPlaceHandIndex);
                    System.out.println("Choose hand index or press [0] to go back to HOME MENU: ");
                    cardToPlaceHandIndex = scanner.nextInt();
                    if (cardToPlaceHandIndex == 0) {
                        this.printer.comeBack(this);
                    }
                }
                X = (scanner.nextInt());
                Y = (scanner.nextInt());
                isFlipped = scanner.nextInt() == 1;
            } catch (InputMismatchException e) {
                System.out.print("Error: Please input valid numbers.");
            }
            this.virtualServer.placeCard(cardToPlaceHandIndex, isFlipped, X, Y);
        } else {
            System.out.println("It's not your turn");
        }
    }
}
