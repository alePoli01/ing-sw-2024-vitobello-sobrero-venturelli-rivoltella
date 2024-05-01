package it.polimi.GC13.view.TUI;

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
    private final List<Integer> serialCommonObjectiveCard = new ArrayList<>();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    private Player player;
    private int choice = 0;
    public static final Deck visualdeck = new Deck();

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.checkForExistingGame();
    }

    /*
        used to update players hand in TUI
     */
    @Override
    public void handUpdate(String playerNickname, int[] availableCard) {
        if (playerNickname.equals(this.player.getNickname())) {
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
        String nickname, gameName;
        int playersNumber = -1;

        //asking for all the contents of the message
        System.out.print("Choose a nickname: ");
        nickname = this.reader.readLine();
        this.player = new Player(nickname);

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
        virtualServer.addPlayerToGame(this.player, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }

    public void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) throws IOException {
        String nickname, gameName;
        int playersNumber = -1;

        System.out.print("Choose a nickname: ");
        nickname = this.reader.readLine();
        this.player = new Player(nickname);

        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game:[" + string + "] --|players in waiting room: " + numCurrPlayer +"|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = this.reader.readLine();
        } while (!gameNameWaitingPlayersMap.containsKey(gameName));

        //massage is ready to be sent
        virtualServer.addPlayerToGame(player, playersNumber, gameName);
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
        if (playerNickname.equals(this.player.getNickname())) {
            System.out.println("You chose " + tokenColor + " token\n");
            System.out.println("\n--- SETUP PHASE [2/2]---");
            System.out.println("Choose which side you would like to place your start card.\n(input the number corresponding the option)\n\t[1] FRONT\n\t[2] BACK");
            this.showHand();
            do {
                try {
                    this.choice = Integer.parseInt(this.reader.readLine());
                } catch (NumberFormatException | IOException e) {
                    System.out.println("Error: Please put a number");
                }
            } while (this.choice < 1 || this.choice > 2);

            this.virtualServer.placeStartCard(this.choice != 1);
        } else {
            System.out.println(playerNickname + " chose " + tokenColor + " token\n");
        }
        this.choice = 0;
    }

    /*
        NOTIFY CLIENTS WHEN A CARD IS PLACED ON ANY BOARD
     */
    @Override
    public void onPositionedCard(String playerNickname, int startCardPlaced, boolean isFlipped) {
        String side = isFlipped ? "back" : "front";
        System.out.println(playerNickname + " positioned " + startCardPlaced + " on " + side);
    }

    /*
        NOTIFY THE CLIENTS ABOUT THE COMMON OBJECTIVE CARD
    */
    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard.addAll(serialCommonObjectiveCard);
        System.out.println("COMMON OBJECTIVE CARDS:");
        serialCommonObjectiveCard.forEach(serialNumber -> showObjectiveCard(List.of(this.serialPrivateObjectiveCard)));
    }

    /*
        METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void chosePrivateObjectiveCard(String playerNickname, int[] privateObjectiveCard) {
        if (playerNickname.equals(this.player.getNickname())) {
            System.out.println("\n--- PRIVATE OBJECTIVE CARD ---");
            for (int i = 0; i < privateObjectiveCard.length; i++) {
                this.showObjectiveCard(List.of(privateObjectiveCard[i]));
                System.out.println("\t\t["+i+"]");
            }
            System.out.print("Chose your private objective card: ");
        }

        do {
            try {
                this.choice = Integer.parseInt(this.reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.out.println("Error: Please put a number");
            }
            if (this.choice != 1 && this.choice != 2) {
                System.out.println("Error: Please choose a number between 1 and 2");
            } else {
                this.virtualServer.chosePrivateObjectiveCard(choice);
            }
        } while (this.choice != 1 && this.choice != 2);
        this.choice = 0;
    }

    /*
        NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
    @Override
    public void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard) {
        if (playerNickname.equals(this.player.getNickname())) {
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
        System.out.println("\t[6] to view other player board");
        System.out.println("\t[7] to send a message in the chat");
        try {
            choice = Integer.parseInt(reader.readLine());
        } catch (IOException e) {
            System.out.print("Error: Please put a number: ");
        }
        switch (choice) {
            case 1: showHand();
            case 2: // TO DO;
            case 3: showObjectiveCard(this.serialCommonObjectiveCard);
            case 4: showObjectiveCard(List.of(this.serialPrivateObjectiveCard));
            case 5: // TO DO;
            case 6: // TO DO;
            case 7: // TO DO;
        }
    }

    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.player.getNickname())) {
            System.out.println(onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
        }
    }

    private void showObjectiveCard(List<Integer> serialNumber) {
        for (ObjectiveCard card : visualdeck.getObjectiveDeck()) {
            if (card.serialNumber == serialNumber.getFirst() || card.serialNumber == serialNumber.getLast()) {
                System.out.println(this.player.getNickname() + " privateObjectiveCard: ");
                card.printObjectiveCard();
            }
        }
    }

    private void showHand() {
        if (this.hand.getFirst() < 81 || this.hand.getFirst() > 86) {
            for (PlayableCard card : visualdeck.getResourceDeck()) {
                for (int x : this.hand) {
                    if (card.serialNumber == x) {
                        card.cardPrinter(false);
                    }
                }
            }
        } else {
            for (StartCard card : visualdeck.getStartDeck()) {
                for (int x : this.hand) {
                    if (card.serialNumber == x) {
                        card.cardPrinter(false);
                        System.out.println("       FRONT");
                        card.cardPrinter(true);
                        System.out.println("        BACK");
                    }
                }
            }
        }
    }
}
