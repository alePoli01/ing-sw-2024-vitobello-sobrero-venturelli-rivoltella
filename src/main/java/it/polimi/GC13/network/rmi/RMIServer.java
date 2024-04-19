package it.polimi.GC13.network.rmi;

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
    public void addPlayerToGame(String nickname) {

    }

    @Override
    public void quitGame() {

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
