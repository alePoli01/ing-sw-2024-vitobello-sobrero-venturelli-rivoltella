package it.polimi.GC13.app;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.socket.ServerDispatcher;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        int rmiPort = 0;
        int socketPort = 0;

        if (args.length != 2) {
            if (args.length < 2) {
                System.err.println("Missing Parameters, killing this server.");
            } else {
                System.err.println("Too many Parameters, killing this server.");
            }
            System.err.println("HINT: use <rmiPort> <socketPort> as Arguments");
            System.exit(-1);
        }

        try {
            rmiPort = Integer.parseInt(args[0]);
            socketPort = Integer.parseInt(args[1]);
        } catch (NumberFormatException e) {
            System.err.println("Illegal Argument Format, killing this client.\nHINT: check port numbers");
            System.exit(-1);
        }


        System.setProperty("java.server.hostname", "localhost");

        System.out.println("\u001B[35mHello from Server\u001B[0m");
        System.out.println("RMI HostName: " + System.getProperty("java.server.hostname"));
        System.out.println("RMI port: " + rmiPort);
        System.out.println("Socket port: " + socketPort + "\n-------------\n");

        // link a lobby controller to the controller dispatcher
        System.out.println("Creating and linking LobbyController and ControllerDispatcher");
        LobbyController lobbyController = new LobbyController();
        ControllerDispatcher controllerDispatcher = new ControllerDispatcher(lobbyController);
        // link the lobby the serverDispatcher that will be linked to the client, so that the lobby will be able to connect the client to the game
        lobbyController.setControllerDispatcher(controllerDispatcher);

        // link a controller(and lobby) dispatcher to a server dispatcher
        System.out.println("Creating and linking ServerDispatcher to ControllerDispatcher(/w LobbyController)");
        ServerDispatcher serverDispatcher = new ServerDispatcher(controllerDispatcher);

        System.out.println("\u001B[33mStarting RMI server...\u001B[0m");
        RMIServer rmiServer = new RMIServer(controllerDispatcher, lobbyController);
        rmiServer.startServer(rmiPort);

        // create an Accepter that will connect the server port and dispatcher to a client
        System.out.println("\u001B[33mStarting Socket server...\u001B[0m");
        SocketAccepter socketAccepter = new SocketAccepter(serverDispatcher, socketPort);

        // start waiting for a client to connect
        new Thread(socketAccepter).start();
    }
}
