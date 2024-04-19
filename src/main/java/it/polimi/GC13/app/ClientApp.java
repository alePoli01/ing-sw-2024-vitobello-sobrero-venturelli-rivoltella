package it.polimi.GC13.app;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIClient;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.TUI.TUI;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;
import java.rmi.NotBoundException;

public class  ClientApp {
    public static void main( String[] args ) throws IOException {
        System.out.println("Hello from Client");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int choice = 0;
        ServerInterface virtualServer = null;

        // INTERNET PROTOCOL CHOICE
        do {
            System.out.println("Chose the connection \n[1] RMI\n[2] SOCKET \nYour choice: ");
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice != 1 && choice != 2);


        if (choice == 1) {
            virtualServer = new RMIServer(321);
            ClientInterface client = new RMIClient(reader.readLine());
            try {
                ((RMIClient) client).startRMIConnection();
                System.out.println("You chose RMI!");
            } catch (NotBoundException e) {
                System.out.println("Binding with server failed");
            } catch (PlayerNotAddedException e) {
                System.out.println(e.getMessage());
            }
        } else {
            System.out.println(choice);
            Socket socket = new Socket("localhost", 123);
            virtualServer = new SocketServer(socket);
            System.out.println("You chose Socket!");
        }

        // VIEW CHOICE
        choice = 0;
        do {
            System.out.println("Chose your view \n[1] TUI\n[2] GUI \nYour choice: ");
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            System.out.println("You chose TUI!");
            TUI tui = new TUI(virtualServer);
        } else {
            System.out.println("You chose GUI!");
        }
    }
}
