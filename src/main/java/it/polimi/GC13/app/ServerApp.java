package it.polimi.GC13.app;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.model.Server;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.rmi.RMIServerInterface;
import it.polimi.GC13.network.socket.ControllerDispatcher;
import it.polimi.GC13.network.socket.ServerDispatcher;

import java.io.IOException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Server");

        int RMIport = 1234;
        RMIServerInterface rmiServer = new RMIServer(RMIport, new Server());
        rmiServer.startServer();

        int socketPort=0;
        //link a lobby controller to the controller dispathcer
        LobbyController lobbyController = new LobbyController();
        ControllerDispatcher controllerDispatcher = new ControllerDispatcher(lobbyController);
        //link  a controller(and lobby) dispatcher to a server dispatcher
        ServerDispatcher serverDispatcher= new ServerDispatcher(controllerDispatcher);
        //create an Accepter that will connect the server port and dispatcher to a client
        SocketAccepter socketAccepter = new SocketAccepter(serverDispatcher, socketPort);
        //link the lobby the serverDispatcher that will be linked to the client, so that the lobby will be able to connect the client to the game
        lobbyController.setControllerDispatcher(controllerDispatcher);
        //start waiting for a client to connect
        new Thread(socketAccepter).start();
    }
}
