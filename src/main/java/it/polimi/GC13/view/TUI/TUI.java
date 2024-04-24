package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class TUI implements View {
    ServerInterface virtualServer;

    public TUI(ServerInterface virtualServer) throws IOException {
        this.virtualServer = virtualServer;
        this.virtualServer.checkForExistingGame();
    }

    @Override
    public void display(OnCheckForExistingGameMessage onCheckForExistingGameMessage, int waitingPlayers) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname;
        int playersNumber = -1;

        System.out.println("Choose a nickname:");
        nickname = reader.readLine();
        /*do {
            String confirm;
            System.out.println("Are you sure? [y] or [n]:");
            confirm = reader.readLine();
        } while (!(confirm.equals("y")));*/

        if (waitingPlayers == 0) {
            //getting number of players
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
        }
        try {
            virtualServer.addPlayerToGame(nickname, playersNumber);
            System.out.println("++Sent: addPlayerToGame");
        } catch (NicknameAlreadyTakenException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void update(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage, int waitingPlayers) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        if (waitingPlayers == 0) {
            System.out.println("--- SETUP PHASE ---");
            System.out.println("[1] to choose token");
            System.out.println("[2] to place start card");
            switch (Integer.parseInt(reader.readLine())) {
                case 1: {
                    System.out.println("Write your token color:");
                    this.virtualServer.chooseToken(TokenColor.valueOf(reader.readLine()));
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

    @Override
    public void display() throws IOException {
        /*
        TODO: BISOGNA CAPIRE COME FAR ARRIVARE ALLA VIEW IL CONTENUTO DEL MESSAGGIO DAL SERVER
              qui controllo se esiste un gioco e nel caso chiedo solo il nick, ma bisogna capire come fare
         */

        System.out.println("++Sending: checkForExistingGame");
        virtualServer.checkForExistingGame();
    }
}
