package it.polimi.GC13.network.rmi;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
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
    public void addPlayerToGame(String nickname, int numOfPlayers, String gameName) throws NicknameAlreadyTakenException {

    }

    @Override
    public void checkForExistingGame() {

    }

    @Override
    public void chooseToken(TokenColor tokenColor) {

    }

    @Override
    public void placeStartCard(boolean side) {

    }

    @Override
    public void writeMessage() {

    }

    @Override
    public void drawCard() {

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
