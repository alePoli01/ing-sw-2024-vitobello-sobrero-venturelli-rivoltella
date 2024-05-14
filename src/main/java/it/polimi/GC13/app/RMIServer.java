package it.polimi.GC13.app;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.rmi.RMIServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private ControllerDispatcher controllerDispatcher;
    private final LobbyController lobbyController;
    private List<ClientInterface> clientList;

    protected RMIServer(ControllerDispatcher controllerDispatcher, LobbyController lobbyController) throws RemoteException {
        this.controllerDispatcher = controllerDispatcher;
        this.lobbyController = lobbyController;
    }

    public void startServer(int port) throws RemoteException {
        try {
            //create registry on selected port
            System.out.println("\t\tcreating registry on port: " + port);
            Registry registry = LocateRegistry.createRegistry(port);
            registry.rebind("server", this);//binds the RMIServer class on the registry under the name server
        } catch (Exception e) {
            System.err.println("Failed to bind server to RMI registry");
        }
        clientList = new ArrayList<>();
        System.out.println("RMI Server is ready");
    }

    @Override
    public void registerMessageFromClient(MessagesFromClient message) throws RemoteException {
        //message.methodToCall(this.lobbyController, this.controllerDispatcher.getCon);
    }

    @Override
    public void login(ClientInterface client) throws RemoteException {
        //forse da eliminare
        clientList.add(client);
    }

    @Override
    public void checkForExistingGame(ClientInterface client) throws RemoteException {
        lobbyController.checkForExistingGame(client);
    }

    @Override
    public void createNewGame(String playerNickname, int numOfPlayers, String gameName, ClientInterface client) throws RemoteException {
    }

    @Override
    public void addPlayerToGame(String playerNickname, String gameName, ClientInterface client) throws RemoteException {

    }

    @Override
    public void chooseToken(TokenColor tokenColor, ClientInterface client) throws RemoteException {

    }

    @Override
    public void placeStartCard(boolean isFlipped, ClientInterface client) throws RemoteException {

    }

    @Override
    public void placeCard(int serialCardToPlace, boolean isFlipped, int X, int Y, ClientInterface client) throws RemoteException {

    }

    @Override
    public void writeMessage(String sender, String receiver, String message, ClientInterface client) throws RemoteException {

    }


    @Override
    public void drawCard(int serialCardToDraw, ClientInterface client) throws RemoteException {

    }

    @Override
    public void choosePrivateObjectiveCard(int serialPrivateObjectiveCard, ClientInterface client) throws RemoteException {

    }
}
