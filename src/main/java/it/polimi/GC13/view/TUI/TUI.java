package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Player;
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
    private final List<Integer> serialPublicObjectiveCard = new ArrayList<>();

    private Player player;

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.checkForExistingGame();
    }

    /*
        used to update players hand in TUI
     */
    @Override
    public void handUpdate(int[] availableCard) {
        this.hand.clear();
        Arrays.stream(availableCard)
                .forEach(this.hand::add);
    }

    @Override
    public void setPrivateObjectiveCard(int serialPrivateObjectiveCard) {
        this.serialPrivateObjectiveCard = serialPrivateObjectiveCard;
    }

    @Override
    public void setSerialPublicObjectiveCard(List<Integer> serialPublicObjectiveCard) {
        this.serialPublicObjectiveCard.clear();
        this.serialPublicObjectiveCard.addAll(serialPublicObjectiveCard);
    }

    @Override
    public void checkForExistingGame() {
        this.virtualServer.checkForExistingGame();
        System.out.println("++Sent: checkForExistingGame");
    }

    /* shows 2 options to the player
    [1] create new game
    [2] join an existing one
     */
    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;

        if (gameNameWaitingPlayersMap.isEmpty()) {
            System.out.println("There is no existing game");
            createNewGame();
        } else {
            //ask what the player wants to do
            System.out.println("There are existing games, choose:\n\t[1] to create a new Game\n\t[2] to join an existing Game");
            do {
                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please put a number");
                }
            } while (choice < 1 || choice > 2);

            if (choice == 1) {
                //player wants to create a new game
                createNewGame();
            } else {
                //player can and wants to join an existing game
                joinExistingGame(gameNameWaitingPlayersMap);
            }
        }
    }

    private void createNewGame() throws IOException {
        /*
        create and send message for a new game
         */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname, gameName;
        int playersNumber = -1;

        //asking for all the contents of the message
        System.out.print("Choose a nickname: ");
        nickname = reader.readLine();
        this.player = new Player(nickname);

        System.out.print("Choose a name for the new Game: ");
        gameName = reader.readLine();

        System.out.print("Choose Number of players in the game [min 2, max 4]: ");
        do {
            try {
                playersNumber = Integer.parseInt(reader.readLine());
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname, gameName;
        int playersNumber = -1;

        System.out.print("Choose a nickname: ");
        nickname = reader.readLine();
        this.player = new Player(nickname);

        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game:[" + string + "] --|players in waiting room: " + numCurrPlayer +"|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = reader.readLine();
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
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tokenColorChosen;
        boolean flag = false;
        StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");

        if (readyPlayers == neededPlayers) {
            tokenColorList.stream().map(TokenColor::toString).forEach(joiner::add);
            System.out.println("\n--- SETUP PHASE [1/2]---");
            System.out.println("Choose your token color: " + joiner);
            do {
                try {
                    tokenColorChosen = reader.readLine().toUpperCase();
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
    public void startCardSetupPhase(String playerNickname, TokenColor tokenColor) throws IOException {
        if (playerNickname.equals(this.player.getNickname())) {
            BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
            int choice = 0;
            System.out.println("You chose " + tokenColor + " token");
            System.out.println("\n--- SETUP PHASE [2/2]---");
            System.out.println("Choose which side you would like to place your start card.\n(input the number corresponding the option)\n[1] front\n[2] back");
            this.showCard();
            do {
                try {
                    choice = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.out.println("Error: Please put a number");
                }
            } while (choice < 1 || choice > 2);

            this.virtualServer.placeStartCard(choice != 1);
        } else {
            System.out.println(playerNickname + " chose " + tokenColor + " token");
        }
    }


    @Override
    public void chosePrivateObjectiveCard(String player, int startCardPlaced, boolean isFlipped) {
        String side = isFlipped ? "back" : "front";
        System.out.println(player + " positioned " + startCardPlaced + " on " + side);

        /*if (readyPlayers == neededPlayers) {
            this.firstNotify = false;
            System.out.println("Chose private objective Card:\n\t[1] to show first choice\n\t[2] to show second choice");
            this.showObjectiveCard(serialPrivateObjectiveCard);
        } else if (!this.firstNotify) {
            this.firstNotify = true;
            System.out.println("--|players that placed starter card: " + readyPlayers + "/" + neededPlayers);
        } else {
            System.out.println("--|players that placed starter card: " + readyPlayers + "/" + neededPlayers);
        }*/
    }

    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.player.getNickname())) {
            System.out.println("Sono nella tui a rilanciare");
            System.out.println(onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
        }
    }

    private void showObjectiveCard(int serialNumber) {
        // call to printer
    }

    private void showCard() {
        // call to printer
    }
}
