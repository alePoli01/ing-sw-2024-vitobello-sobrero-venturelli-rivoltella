package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.inputException.InputException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Map;
import java.util.StringJoiner;

public class TUI implements View {
    ServerInterface virtualServer;

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.virtualServer.checkForExistingGame();
        System.out.println("++Sent: checkForExistingGame");
    }

    /* shows 2 options to the player
    [1] create new game
    [2] join an existing one
     */
    @Override
    public void joiningPhase(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        int choice = 0;

        if (joinableGameMap.isEmpty()) {
            System.out.println("There is no existing game");
            createNewGame();
        } else {
            //ask what the player wants to do
            System.out.println("There are existing games, choose:\n\t[1] to create a new Game\n\t[2] to join an existing Game ");
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
                joinExistingGame(waitingPlayersMap, joinableGameMap);
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
        System.out.print("Choose a nickname:");
        nickname = reader.readLine();

        System.out.print("Choose a name for the new Game:");
        gameName = reader.readLine();
        System.out.println("Choose Number of players in the game [min 2, max 4]:");
        do {
            try {
                playersNumber = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
            if (playersNumber < 2 || playersNumber > 4) {
                System.out.println("Error: Please choose a number between 2 and 4");
            }
        } while (playersNumber < 2 || playersNumber > 4);

        //massage is ready to be sent
        try {
            virtualServer.addPlayerToGame(nickname, playersNumber, gameName);
            System.out.println("++Sent: addPlayerToGame");
        } catch (NicknameAlreadyTakenException e) {
            System.out.println(e.getMessage());
        }
    }

    public void joinExistingGame(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname, gameName;
        int playersNumber = -1;

        System.out.print("Choose a nickname:");
        nickname = reader.readLine();
        System.out.println("Joinable Games:");
        joinableGameMap.forEach((string, game) -> System.out.println("\t>game:[" + string + "] --|players in waiting room: " + waitingPlayersMap.get(game)+"|"));
        do {
            System.out.println("Select the game to join unsing its name");
            gameName = reader.readLine();
        } while (!joinableGameMap.containsKey(gameName));

        //massage is ready to be sent
        try {
            virtualServer.addPlayerToGame(nickname, playersNumber, gameName);
            System.out.println("++Sent: addPlayerToGame");
        } catch (NicknameAlreadyTakenException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
    shows SETUP PHASE methods to the player
    [1] chose the token
    [2] place the start card
     */
    @Override
    public void tokenSetupPhase(int waitingPlayers, List<TokenColor> tokenColorList, int playersNeeded) {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tokenColorChosen;
        boolean flag = false;
        StringJoiner joiner = new StringJoiner(" / ", "[ ", " ]");

        if (waitingPlayers == 0) {
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
                    System.out.println("Colore ricevuto correttamente: " + finalTokenColor + "\n++Sending to controller");
                    // calls the controller to update the model
                    this.virtualServer.chooseToken(TokenColor.valueOf(tokenColorChosen));
                } else {
                    System.out.println("Color not valid, you can chose: " + joiner);
                }
            } while (!flag);
        } else {
            System.out.println("--|players in waiting room: " + waitingPlayers+"/"+playersNeeded);
        }
    }

    @Override
    public void startCardSetupPhase(TokenColor tokenColor) throws IOException {
        /*
            TODO tutto
        */
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        System.out.println("\n--- SETUP PHASE [2/2]---");
        System.out.println("Choose which side you would like to place your start card.\n[1] front\n[2] back\n[3] show card");
    }


    // used to print any input error (at the moment handles token color) and recall the method from the TUI
    @Override
    public void exceptionHandler(InputException e) {
        System.out.println("sono nella tui a rilanciare");
        try {
            System.out.println(e.getMessage());
            e.methodToRecall(this);
        } catch (IOException e1) {
            throw new RuntimeException(e1);
        }
    }
}
