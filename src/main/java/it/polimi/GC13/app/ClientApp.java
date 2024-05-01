package it.polimi.GC13.app;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIClient;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.TUI.TUI;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        //SwingUtilities.invokeLater(MainPage::new); //per testare la grafica
        //SwingUtilities.invokeLater(LoginFrame::new);
        System.out.println("Hello from Client");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

        int choice = 0;
        ServerInterface virtualServer;
        ClientDispatcher clientDispatcher = null;

        // INTERNET PROTOCOL CHOICE
        do {
            System.out.print("Chose the connection:\n\t[1] RMI or [2] SOCKET\nYour choice: ");
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
            }
        } else {
            Socket socket = new Socket("localhost", 123); // creating socket that represents the server
            clientDispatcher = new ClientDispatcher();
            virtualServer = new SocketServer(socket, clientDispatcher,clientDispatcher); //the connection is socket so the virtual server is a SocketServer object
            new Thread((SocketServer) virtualServer).start();
            System.out.println("--|You chose Socket!|--");
        }

        // VIEW CHOICE
        choice = 0;
        do {
            System.out.print("Chose your view:\n\t[1] TUI or [2] GUI\nYour choice: ");
            try {
                choice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (choice != 1 && choice != 2);

        if (choice == 1) {
            System.out.println("You chose TUI!");
            if (clientDispatcher != null) {
                clientDispatcher.setView(new TUI(virtualServer));
            }
        } else {
            System.out.println("You chose GUI!");
        }
    }
}
