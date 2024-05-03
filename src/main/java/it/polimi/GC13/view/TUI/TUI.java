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
    ServerInterface virtualServer;
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
        }
        this.choice = 0;
    }

    private void createNewGame() throws IOException {
        /*
        create and send message for a new game
         */
        String gameName;
        int playersNumber = -1;

        //asking for all the contents of the message
        System.out.print("Choose a nickname: ");
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
        virtualServer.addPlayerToGame(this.nickname, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    private void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) throws IOException {
        String gameName;
        int playersNumber = -1;

        System.out.print("Choose a nickname: ");
        this.nickname = this.reader.readLine();

        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game:[" + string + "] --|players in waiting room: " + numCurrPlayer +"|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = this.reader.readLine();
        } while (!gameNameWaitingPlayersMap.containsKey(gameName));

        //massage is ready to be sent
        virtualServer.addPlayerToGame(this.nickname, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    /*
        SETUP PHASE methods to the player
        tokenSetupPhase to chose your token
     */
    @Override
    public void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers) {
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
    public void startCardSetupPhase(String playerNickname, TokenColor tokenColor) {
        if (playerNickname.equals(this.nickname)) {
            System.out.println("You chose " + tokenColor + " token\n");
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
        } else {
            this.gamesLog.add(playerNickname + " chose " + tokenColor + " token\n");
        }
        this.choice = 0;
    }

    /*
        NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void onPositionedCard(String playerNickname, int startCardPlaced, boolean isFlipped) {
        String message = playerNickname + " positioned " + startCardPlaced + " on " + (isFlipped ? "back" : "front");
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
        }

        try {
            while (choice != 1 && choice != 2) {
                System.out.print("Chose your private objective card [1] or [2]: ");
                this.choice = Integer.parseInt(this.reader.readLine());
            }
            this.virtualServer.chosePrivateObjectiveCard(choice);
            System.out.println("++Sent private objective card choice message");
        } catch (NumberFormatException | IOException e) {
            System.out.print("Error: Please put a number.\nChose your private objective card [1] or [2]: ");
        }
        this.choice = 0;
    }

    /*
        NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
    @Override
    public void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard) {
        if (playerNickname.equals(this.nickname)) {
            this.serialPrivateObjectiveCard = indexPrivateObjectiveCard;
            System.out.println("Your private objective card is " + indexPrivateObjectiveCard);
            this.showHomeMenu();
        }
    }


    /*
        METHOD TO SHOW CLIENT ALL POSSIBLE MOVES
     */
    private void showHomeMenu() {
        System.out.println("\n--- HOME MENU ---");
        System.out.println("\t[1] to view your hand");
        System.out.println("\t[2] to place a card (only when in turn)");
        System.out.println("\t[3] to view common objective card");
        System.out.println("\t[4] to view your private objective card");
        System.out.println("\t[5] to view your board");
        System.out.println("\t[6] to view other players' board");
        System.out.println("\t[7] to send a message in the chat");
        System.out.println("\t[8] to show game's history");
        System.out.println("\t[9] to show players' turns");
        System.out.print("Your choice: ");
        try {
            choice = Integer.parseInt(reader.readLine());
            System.out.println("SELECTION: " + choice);
        } catch (IOException e) {
            System.out.print("Error: Please put a number: ");
        }
        switch (choice) {
            case 1: {
                this.printer.showHand(this.hand);
                this.printer.comeBack();
            }
            case 2: this.placeCard();
            case 3: {
                this.printer.showObjectiveCard("\n--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
                this.printer.comeBack();
            }
            case 4: {
                this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", this.serialPrivateObjectiveCard);
                this.printer.comeBack();
            }
            case 5: // TO DO;
            case 6: // TO DO;
            case 7: // TO DO;
            case 8: this.printer.showHistory(this.gamesLog);
            case 9: System.out.println(this.playerPositions);
        }
    }

    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            System.out.println("Sono a rilanciare");
            System.out.println(onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
        }
    }

    @Override
    public void displayAvailableCells(Set<Coordinates> availableCells) {
        System.out.println("Available cells are: ");
        System.out.println(availableCells);
    }

    @Override
    public void connectionLost() {
        try {
            this.wait();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void displayTurns(Map<String, Position> playerPositions) {
        this.playerPositions.putAll(playerPositions);
    }

    /*
        NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
        OTHERS -> ADDS TO LOG OPERATION
     */
    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.turn = turn;
            if (this.turn) {
                System.out.println("\nIt's your turn");
            } else {
                System.out.println("\nYou passed the turn");
            }
        } else if (turn) {
            this.gamesLog.add("\nIt's" + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
    }

    /*
        METHOD USED TO PLACE CARD ON THE BOARD
     */
    private void placeCard() {
        if (this.turn) {
            Coordinates xy = new Coordinates(0, 0);
            int cardToPlaceHandIndex = 0;
            boolean isFlipped = false;
            this.printer.showHand(this.hand);
            System.out.println("Enter hand index [1 -> 3], X coordinate, Y coordinate, and [1] for FRONT [2] for BACK");
            System.out.println("example: 1 (for index), 50 (for X), 49 (for Y) , 1 (for side)");
            Scanner scanner = new Scanner(System.in);
            try {
                cardToPlaceHandIndex = scanner.nextInt();
                while (this.hand.get(cardToPlaceHandIndex) == null) {
                    System.out.println("You don't have a card in position:" + cardToPlaceHandIndex);
                    System.out.println("Choose hand index or press [0] to go back to HOME MENU: ");
                    cardToPlaceHandIndex = scanner.nextInt();
                    if (cardToPlaceHandIndex == 0) {
                        this.printer.comeBack();
                    }
                }
                xy.setX(scanner.nextInt());
                xy.setY(scanner.nextInt());
                isFlipped = scanner.nextInt() == 1;
            } catch (InputMismatchException e) {
                System.out.print("Error: Please input valid numbers.");
            }
            this.virtualServer.placeCard(cardToPlaceHandIndex, isFlipped, xy);
            this.showHomeMenu();
        } else {
            System.out.println("It's not your turn");
            this.showHomeMenu();
        }
    }
}
