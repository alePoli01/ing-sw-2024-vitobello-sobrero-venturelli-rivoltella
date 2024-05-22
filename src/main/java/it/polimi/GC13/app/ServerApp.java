package it.polimi.GC13.app;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.network.socket.ServerDispatcher;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;

public class ServerApp {
    public static void main(String[] args) throws IOException {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
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

        System.out.println("\u001B[35mHello from Server\u001B[0m");
        System.out.println("RMI HostName: " + System.getProperty("java.server.hostname"));
        System.out.println("RMI port: " + RMIport);
        System.out.println("Socket port: " + socketPort+"\n-------------\n");

        // link a lobby controller to the controller dispatcher
        System.out.println("Creating and linking LobbyController and ControllerDispatcher");
        LobbyController lobbyController = new LobbyController();
        ControllerDispatcher controllerDispatcher = new ControllerDispatcher(lobbyController);
        // link the lobby the serverDispatcher that will be linked to the client, so that the lobby will be able to connect the client to the game
        lobbyController.setControllerDispatcher(controllerDispatcher);
        restartGames(lobbyController, controllerDispatcher);

        // link a controller(and lobby) dispatcher to a server dispatcher
        System.out.println("Creating and linking ServerDispatcher to ControllerDispatcher(/w LobbyController)");
        ServerDispatcher serverDispatcher= new ServerDispatcher(controllerDispatcher);

        System.out.println("\u001B[33mStarting RMI server...\u001B[0m");
        RMIServer rmiServer = new RMIServer(controllerDispatcher,lobbyController);
        rmiServer.startServer(RMIport);

        // create an Accepter that will connect the server port and dispatcher to a client
        System.out.println("\u001B[33mStarting Socket server...\u001B[0m");
        SocketAccepter socketAccepter = new SocketAccepter(serverDispatcher, socketPort);

        // start waiting for a client to connect
        new Thread(socketAccepter).start();
    }

    public static void restartGames(LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        File gamesFolder = new File("");
        File[] listOfGame = gamesFolder.listFiles((dir, name) -> name.endsWith(".ser"));
        if (listOfGame != null) {
            DiskManager diskManager = new DiskManager();
            for (File file : listOfGame) {
                if (file.isFile()) {
                    System.out.println("Found serialized file: " + file.getName());
                }
                lobbyController.getStartedGameMap().put(file.getName(), diskManager.readFromDisk(file.getName()));
            }
            lobbyController.getStartedGameMap().values()
                    .forEach(game -> lobbyController.getGameControllerMap().put(game, new Controller(game, lobbyController, controllerDispatcher)));
            System.out.println("Finished restarting games");
        }
    }
}
