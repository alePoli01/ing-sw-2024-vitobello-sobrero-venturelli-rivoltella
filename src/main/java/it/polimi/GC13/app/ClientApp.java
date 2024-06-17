package it.polimi.GC13.app;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientApp {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int rmiPort = 0;
        int socketPort = 0;
        int connectionChoice = 0;
        ServerInterface virtualServer;
        ClientDispatcher clientDispatcher;
        View view;
        String rmiHostname = null;

        if (args.length < 2) {
            System.err.println("Missing Arguments, killing this server.");
            System.err.println("HINT: use <server-name> <rmiPort> <socketPort> as Arguments");
            System.exit(-1);
        } else if (args.length == 2) {
            try {
                rmiPort = Integer.parseInt(args[0]);
                socketPort = Integer.parseInt(args[1]);
                System.out.println("Insert address of the Server:");
                rmiHostname = reader.readLine();
            } catch (NumberFormatException e) {
                System.err.println("Illegal Argument Format, killing this client.\nHINT: check port numbers");
                System.exit(-1);
            }
        } else {
            try {
                rmiPort = Integer.parseInt(args[1]);
                socketPort = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Illegal Argument Format, killing this client.");
                System.exit(-1);
            }
            rmiHostname = args[0];
        }
        System.setProperty("java.server.hostname", rmiHostname);

        System.out.println("\u001B[35mHello from Client\u001B[0m");
        System.out.println("RMI port: " + rmiPort);
        System.out.println("Socket port: " + socketPort + "\n");

        // INTERNET PROTOCOL CHOICE
        do {
            System.out.print("Chose the connection:\n\t[1] RMI or [2] SOCKET\nYour choice: ");
            try {
                connectionChoice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (connectionChoice != 1 && connectionChoice != 2);

        // VIEW CHOICE
        int viewChoice = 0;
        do {
            System.out.print("Chose your view:\n\t[1] TUI or [2] GUI\nYour choice: ");
            try {
                viewChoice = Integer.parseInt(reader.readLine());
            } catch (NumberFormatException e) {
                System.err.println("Invalid value!");
            }
        } while (viewChoice != 1 && viewChoice != 2);

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(viewChoice, connectionChoice, socketPort, rmiPort);

        System.out.println("\u001B[33mStarting " + (viewChoice == 1 ? "TUI" : "GUI") + " with connection type " + (connectionChoice == 1 ? "RMI" : "SOCKET") + "\u001B[0m");
        clientDispatcher = new ClientDispatcher();

        view = connectionBuilder.createView();
        clientDispatcher.setView(view);
        try {
            virtualServer = connectionBuilder.createServerConnection(clientDispatcher);
            view.setVirtualServer(virtualServer);
            view.startView();
        } catch (IOException e) {
            System.err.println(e.getMessage());
            System.exit(-1);
        }
    }
}