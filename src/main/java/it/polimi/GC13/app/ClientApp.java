package it.polimi.GC13.app;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIClientAdapter;
import it.polimi.GC13.network.rmi.RMIClientImpl;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.TUI.TUI;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


public class ClientApp {
    // SwingUtilities.invokeLater(LoginFrame::new);
    public static void main(String[] args) throws IOException {

        if (args.length != 2) {
            System.err.println("Missing Parameters, killing this server.");
            System.err.println("HINT: metti | 'nome-server' 1099 456 | come parametri nella run configuration di ClientApp");
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

        /*
        quando vuoi dichiarare la scheda di rete usa -D 'copia dal progetto degli antichi'
        */
        System.out.println("Hello from Client");
        System.out.println("RMI port: " + RMIport);
        System.out.println("Socket port: " + socketPort + "\n");
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));

        int connectionChoice = 0;
        ServerInterface virtualServer = null;
        ClientDispatcher clientDispatcher;
        View view = null;

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

        System.out.println("\u001B[33mStarting " + (viewChoice == 1 ? "TUI" : "GUI") + " with connection type " + (connectionChoice == 1 ? "RMI" : "SOCKET") + "\u001B[0m");
        clientDispatcher = new ClientDispatcher();
        if (viewChoice == 1) {
            view = new TUI();
        } else {
            view = new FrameManager();
        }
        clientDispatcher.setView(view);

        if (connectionChoice == 1) {
            // RMI SETUP
            RMIClientAdapter rmiClientAdapter = new RMIClientAdapter(clientDispatcher);

            //virtualServer becomes RMIServer
            virtualServer = rmiClientAdapter.startRMIConnection("localhost", RMIport);
            System.out.println("Connection completed");
        } else {
            // SOCKET SETUP
            try {
                Socket socket = new Socket("localhost", socketPort); // creating socket that represents the server
                virtualServer = new SocketServer(socket, clientDispatcher); //the connection is socket so the virtual server is a SocketServer object
                new Thread((SocketServer) virtualServer).start();
            } catch (IOException e) {
                System.err.println("Failed to create socket.");
            }
        }

        view.setVirtualServer(virtualServer);
        view.startView();
    }
}