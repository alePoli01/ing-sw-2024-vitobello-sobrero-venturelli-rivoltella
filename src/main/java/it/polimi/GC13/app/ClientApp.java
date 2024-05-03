package it.polimi.GC13.app;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIClient;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.TUI.TUI;

import javax.swing.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.function.Function;

public class ClientApp {
    // SwingUtilities.invokeLater(MainPage::new); //per testare la grafica
    // SwingUtilities.invokeLater(LoginFrame::new);
    public static void main(String[] args) throws IOException {
        if (args.length != 2) {
            System.err.println("Missing Parameters, killing this server.");
            System.err.println("HINT: metti | 'nome-server' 123 456 | come parametri nella run configuration di ClientApp");
            System.exit(-1);
        }

        int RMIport = 0;
        int socketPort = 0;

        try {
            RMIport = Integer.parseInt(args[0]);
            socketPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Illegal Argument Format, killing this client.");
            System.exit(-1);
        }
       // System.setProperty("java.rmi.server.hostname", args[0]);

        //SwingUtilities.invokeLater(MainPage::new); //per testare la grafica
        //SwingUtilities.invokeLater(LoginFrame::new);
        /*
        quando vuoi dichiarare la scheda di rete usa -D 'copia dal progetto degli antichi'
        */
        System.out.println("Hello from Client");
        System.out.println("RMI port: " + RMIport);
        System.out.println("Socket port: " + socketPort+"\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

        int connectionChoice = 0;
        ServerInterface virtualServer = null;
        ClientDispatcher clientDispatcher = null;

        // INTERNET PROTOCOL CHOICE
        do {
            System.out.print("Chose the connection:\n\t[1] RMI or [2] SOCKET\nYour choice: ");
            try {
                connectionChoice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.err.println("Invalid value!");
            }
        } while (connectionChoice != 1 && connectionChoice != 2);

        // VIEW CHOICE
        int viewChoice = 0;
        do {
            System.out.print("Chose your view:\n\t[1] TUI or [2] GUI\nYour choice: ");
            try {
                viewChoice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException | IOException e) {
                System.err.println("Invalid value!");
            }
        } while (viewChoice != 1 && viewChoice != 2);

        Function<Integer, String> viewChoiceMessage = (c) -> {
            if (c == 1) {
                return "TUI";
            } else {
                return "GUI";
            }
        };
        Function<Integer, String> connectionChoiceMessage = (c) -> {
            if (c == 1) {
                return "RMI";
            } else {
                return "SOCKET";
            }
        };

        System.out.println("Starting "+ viewChoiceMessage.apply(viewChoice)+" with connection type "+ connectionChoiceMessage.apply(connectionChoice));



        // RMI SETUP
        if (connectionChoice == 1) {
            try {
                virtualServer = new RMIServer(RMIport);
                ClientInterface client = new RMIClient(reader.readLine());
                ((RMIClient) client).startRMIConnection();
            } catch (NotBoundException | IOException e) {
                System.err.println("Binding with server failed");
            }
        // SOCKET SETUP
        } else {
            try {
                Socket socket = new Socket("localhost", socketPort); // creating socket that represents the server
                clientDispatcher = new ClientDispatcher();
                virtualServer = new SocketServer(socket, clientDispatcher, clientDispatcher); //the connection is socket so the virtual server is a SocketServer object
                new Thread((SocketServer) virtualServer).start();
            } catch (IOException e) {
                System.err.println("Failed to create socket!");
            }
        }

        if (viewChoice == 1) {
            if (clientDispatcher != null) {
                clientDispatcher.setView(new TUI(virtualServer));
            }
        } else {
            System.out.println("GUI is WIP! (babbo)");
        }
    }
}