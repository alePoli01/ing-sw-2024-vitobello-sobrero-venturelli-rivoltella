package it.polimi.GC13.view.TUI;

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
        String nickname, confirm;
        int playersNumber = -1;
        do {
            System.out.println("Choose a nickname:");
            nickname = reader.readLine();
            System.out.println("Are you sure? [y] or [n]:");
            confirm = reader.readLine();
        } while (!(confirm.equals("y")));

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
        if (waitingPlayers == 0) {
            System.out.println("Vari metodi di gioco");
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
