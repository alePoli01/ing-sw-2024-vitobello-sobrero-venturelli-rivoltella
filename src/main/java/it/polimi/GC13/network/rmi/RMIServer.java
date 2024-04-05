package it.polimi.GC13.network.rmi;

import it.polimi.GC13.model.Server;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;

public class RMIServer extends UnicastRemoteObject implements RMIServerInterface {
    private final Server server;
    private Registry registry;
    private final int port;

    public RMIServer(int port, Server server) throws RemoteException {
        this.server = server;
        this.port = port;
    }

    @Override
    public void startServer() throws RemoteException {
        try {
            this.registry = LocateRegistry.createRegistry(this.port);
            this.registry.rebind("server", this);
        } catch (Exception e){
            System.err.println("Failed to bind to RMI registry");
        }
        System.out.println("Server ready");
    }

    @Override
    public void createGame(ClientInterface client) throws IOException {
        this.server.getLobbyController().addPlayerToGame(client.getPlayer());
    }

}
