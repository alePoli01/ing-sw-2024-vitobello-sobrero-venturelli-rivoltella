package it.polimi.GC13.app;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ClientApp {
    // SwingUtilities.invokeLater(LoginFrame::new);
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
        int RMIport = 0;
        int socketPort = 0;
        String rmiHostname = null;

        if (args.length < 2) {
            System.err.println("Missing Parameters, killing this server.");
            System.err.println("HINT: metti | 'nome-server'(a cui ti vuoi collegare in RMI) 1099 456 | come parametri nella run configuration di ClientApp");
            System.exit(-1);
        } else if (args.length == 2) {
            try {
                RMIport = Integer.parseInt(args[0]);
                socketPort = Integer.parseInt(args[1]);
            } catch (NumberFormatException e) {
                System.err.println("Illegal Argument Format, killing this client.\nHINT: check port numbers");
                System.exit(-1);
            }
            System.out.println("Insert address of the Server:");
            rmiHostname = reader.readLine();
        } else {
            try {
                RMIport = Integer.parseInt(args[1]);
                socketPort = Integer.parseInt(args[2]);
            } catch (NumberFormatException e) {
                System.err.println("Illegal Argument Format, killing this client.");
                System.exit(-1);
            }
            rmiHostname = args[0];
        }
        System.setProperty("java.server.hostname", rmiHostname);

        /*
            quando vuoi dichiarare la scheda di rete usa -D 'copia dal progetto degli antichi'
        */
        System.out.println("\u001B[35mHello from Client\u001B[0m");
        System.out.println("RMI port: " + RMIport);
        System.out.println("Socket port: " + socketPort + "\n");


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

        ConnectionBuilder connectionBuilder = new ConnectionBuilder(viewChoice, connectionChoice, socketPort, RMIport);

        System.out.println("\u001B[33mStarting " + (viewChoice == 1 ? "TUI" : "GUI") + " with connection type " + (connectionChoice == 1 ? "RMI" : "SOCKET") + "\u001B[0m");
        clientDispatcher = new ClientDispatcher();

        view = connectionBuilder.createView();
        clientDispatcher.setView(view);
        virtualServer = connectionBuilder.createServerConnection(clientDispatcher);
        view.setVirtualServer(virtualServer);
        view.startView();
    }
}