package it.polimi.GC13.view.TUI;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.InputMismatchException;
import java.util.Scanner;

public class TUI implements View {
    ServerInterface virtualServer;

    public TUI(ServerInterface virtualServer) throws IOException {
        this.virtualServer = virtualServer;
        this.display();
    }

    @Override
    public void display() throws IOException {
        /*
        TODO: BISOGNA CAPIRE COME FAR ARRIVARE ALLA VIEW IL CONTENUTO DEL MESSAGGIO DAL SERVER
              qui controllo se esiste un gioco e nel caso chiedo solo il nick, ma bisogna capire come fare
         */
        virtualServer.checkForExistingGame();
        //getting nickname
        Scanner in = new Scanner(System.in);
        BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
        String nickname, confirm;

        do {
            System.out.println("Choose a nickname:");
            nickname = reader.readLine();
            System.out.println("Are you sure? [y] or [n]:");
            confirm = reader.readLine();
        } while (!(confirm.equals("y")));

        //getting number of players
        int choice=0;
        System.out.println("Choose Number of players in the game [min 2, max 4]:");
        do {
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
            if (choice < 2 || choice > 4) {
                System.out.println("Error: Please choose a number between 2 and 4");
            }
        } while (choice < 2 || choice > 4);

        do {
            try {
                System.out.println("++Sending: addPlayerToGame");
                virtualServer.addPlayerToGame(nickname,choice);
                break;
            } catch (NicknameAlreadyTakenException e) {
                System.out.println(e.getMessage());
            }
        } while(true);
        System.out.println("++Sending: checkForExistingGame");
        virtualServer.checkForExistingGame();
    }
}
