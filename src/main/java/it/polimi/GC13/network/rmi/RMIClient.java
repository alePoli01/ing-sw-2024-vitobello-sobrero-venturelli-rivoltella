package it.polimi.GC13.network.rmi;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Map;

public class RMIClient extends UnicastRemoteObject implements ClientInterface {
    public RMIServerInterface rmiServer;
    public Player player;

    public RMIClient(String nickname) throws RemoteException {
        Player player = new Player(nickname);
    }

    @Override
    public void sendMessage(MessagesFromServer message) {

    }

    @Override
    public void startRMIConnection() throws IOException, NotBoundException {
        Registry registry;
        registry = LocateRegistry.getRegistry("127.0.0.1", 1234);
        this.rmiServer = (RMIServerInterface) registry.lookup("server");
        //this.rmiServer.createGame(this, getPlayer());
    }

    @Override
    public void poke() {

    }
}
