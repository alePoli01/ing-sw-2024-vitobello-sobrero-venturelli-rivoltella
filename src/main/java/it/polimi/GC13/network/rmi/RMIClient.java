package it.polimi.GC13.network.rmi;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

public class RMIClient extends UnicastRemoteObject implements ClientInterface {
    public RMIServerInterface rmiServer;
    public Player player;

    public RMIClient(String nickname) throws RemoteException {
        Player player = new Player(nickname);
    }

    @Override
    public void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException {
        Registry registry;
        registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        this.rmiServer = (RMIServerInterface) registry.lookup("server");
        //this.rmiServer.createGame(this, getPlayer());
    }

    @Override
    public void onCheckForExistingGame(boolean noExistingGames) {
    }

    @Override
    public void poke() throws IOException {

    }
}
