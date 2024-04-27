package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Arrays;
import java.util.Map;

public class TUI implements View {
    ServerInterface virtualServer;

    public TUI(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.virtualServer.checkForExistingGame();
    }

    /* shows 2 options to the player
    [1] create new game
    [2] join an existing one
     */
    @Override
    public void display(OnCheckForExistingGameMessage onCheckForExistingGameMessage, Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException{
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname, gameName = null;
        int choice = 0;
        int playersNumber = -1;

        System.out.println("Choose a nickname:");
        nickname = reader.readLine();
        System.out.println("[1] to create a new Game\n[2] to join an existing Game ");

        do {
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
            // details for a newGame
            if (choice == 1) {
                System.out.println("Choose a name for the new Game:");
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
                    //System.out.println(playersNumber);
                } while (playersNumber < 2 || playersNumber > 4);
                // asks for the game to join
            } else if (choice == 2) {
                if (joinableGameMap.isEmpty()) {
                    System.out.println("There is no existing game");
                } else {
                    System.out.println("Here is the list of the existing Games: ");
                    joinableGameMap.forEach((string, game) -> System.out.println("game: " + string + " waiting players: " + waitingPlayersMap.get(game)));
                    do {
                        System.out.println("Select the game to join with its name");
                        gameName = reader.readLine();
                    } while (!joinableGameMap.containsKey(gameName));
                }
            } else {
                System.out.println("Error: Please choose 1 or 2");
            }
        } while (choice != 1 && choice != 2);

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
    public void setupPhase(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage, int waitingPlayers) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String tokenColorChosen;
        boolean flag = false;

        if (waitingPlayers == 0) {
            System.out.println("--- SETUP PHASE ---");
            System.out.println("[1] to choose token");
            System.out.println("[2] to place start card");
            switch (Integer.parseInt(reader.readLine())) {
                case 1: {
                    System.out.println("Write your token color:");
                    do {
                        tokenColorChosen = reader.readLine();
                        String finalTokenColor = tokenColorChosen;
                        if (Arrays.stream(TokenColor.values()).anyMatch(tc -> tc.name().equalsIgnoreCase(finalTokenColor))) {
                            flag = true;
                            // calls the controller to update the model
                            this.virtualServer.chooseToken(TokenColor.valueOf(tokenColorChosen));
                        } else {
                            System.out.println("Color not valid, you can chose" + Arrays.toString(TokenColor.values()));
                        }
                    } while (!flag);

                    /*
                    TODO risposta del server nel caso il token sia già stato selezionato.
                           se vogliamo gestirlo con le eccezioni, dobbiamo creare un metodo nella tui che viene usato
                           dal server per propagare le eccezioni e stamparle all'utente
                     */
                }
                case 2: {
                    System.out.println("Choose which side you would like to place your start card.\n[1] front [2] back");
                    /*
                    TODO tutto
                     */
                }
                default: System.out.println("Error select: [1] to choose token, [2] to place start card");
            }
        } else {
            System.out.println("waiting players: " + waitingPlayers);
        }
    }

    // used to print exception caught by the controller
    @Override
    public void printExceptionError(Exception e) {
        System.out.println(e.getMessage());
    }
}
