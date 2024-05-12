package it.polimi.GC13.app;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.socket.ServerDispatcher;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {

        if(args.length != 2) {
            System.out.println("Missing Parameters, killing this server.");
            System.out.println("HINT: metti | 'nome-server' 1099 456 | come parametri nella run configuration di ServerApp");
            System.exit(-1);
        }
        int RMIport = 0;
        int socketPort = 0;
        try{
            RMIport = Integer.parseInt(args[0]);
            socketPort = Integer.parseInt(args[1]);

        } catch(NumberFormatException e) {
            System.out.println("Illegal Argument Format, killing this server.");
            System.exit(-1);
        }
        System.setProperty("java.rmi.server.hostname", "127.0.0.1");
        System.out.println("Hello from Server");
        System.out.println("RMI port: " + RMIport);
        System.out.println("Socket port: " + socketPort+"\n-------------\n");
        //link a lobby controller to the controller dispatcher
        System.out.println("Creating and linking LobbyController and ControllerDispatcher");
        LobbyController lobbyController = new LobbyController();
        ControllerDispatcher controllerDispatcher = new ControllerDispatcher(lobbyController);
        //link  a controller(and lobby) dispatcher to a server dispatcher
        System.out.println("Creating and linking ServerDispatcher to ControllerDispatcher(/w LobbyController)");
        ServerDispatcher serverDispatcher= new ServerDispatcher(controllerDispatcher);

        System.out.println("\u001B[33mStarting RMI server...\u001B[0m");
        RMIServer rmiServer = new RMIServer(controllerDispatcher,lobbyController);
        rmiServer.startServer(RMIport);



        //create an Accepter that will connect the server port and dispatcher to a client
        System.out.println("\u001B[33mStarting Socket server...\u001B[0m");
        SocketAccepter socketAccepter = new SocketAccepter(serverDispatcher, socketPort,lobbyController);
        //link the lobby the serverDispatcher that will be linked to the client, so that the lobby will be able to connect the client to the game
        lobbyController.setControllerDispatcher(controllerDispatcher);
        //start waiting for a client to connect
        new Thread(socketAccepter).start();
    }
}
