package it.polimi.GC13.network.rmi;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ServerInterface;

import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements ServerInterface {
    private Registry registry;
    private final int port;

    public RMIServer(int port) throws RemoteException {
        this.port = port;
    }

    @Override
    public void createNewGame(String playerNickname, int numOfPlayers, String gameName) {

    }

    @Override
    public void addPlayerToGame(String playerNickname, String gameName) {

    }

    @Override
    public void checkForExistingGame() {

    }

    @Override
    public void chooseToken(TokenColor tokenColor) {

    }

    @Override
    public void placeStartCard(boolean isFlipped) {

    }

    @Override
    public void placeCard(int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {

    }

    @Override
    public void writeMessage() {

    }

    @Override
    public void drawCard(int cardDeckSerial) {

    }

    @Override
    public void choosePrivateObjectiveCard(int indexPrivateObjectiveCard) {

    }

    public void startServer() throws RemoteException {
        try {
            this.registry = LocateRegistry.createRegistry(this.port);
            this.registry.rebind("server", this);
        } catch (Exception e){
            System.err.println("Failed to bind to RMI registry");
        }
        System.out.println("Server ready");
    }
}
