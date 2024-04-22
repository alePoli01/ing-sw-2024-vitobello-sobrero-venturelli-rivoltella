package it.polimi.GC13.app;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIClient;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.GUI.game.MainPage;
import it.polimi.GC13.view.TUI.TUI;


import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.Socket;
import java.rmi.NotBoundException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Client");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

        int choice = 0;
        ServerInterface virtualServer = null;

        // INTERNET PROTOCOL CHOICE
        do {
            System.out.println("Chose the connection: RMI[1] or SOCKET[2]\nYour choice: ");
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
            Socket socket = new Socket("localhost", 123); // creating socket that represents the server
            ClientDispatcher clientDispatcher = new ClientDispatcher();
            virtualServer = new SocketServer(socket, clientDispatcher); //the connection is socket so the virtual server is a SocketServer object
            new Thread((SocketServer) virtualServer).start();
            System.out.println("--|You chose Socket!|--");
        }

        // VIEW CHOICE
        choice = 0;
        do {
            System.out.println("Chose your view: TUI[1] or GUI[2]\nYour choice: ");
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
        //SwingUtilities.invokeLater(MainPage::new); //per testare la grafica
    }
}
