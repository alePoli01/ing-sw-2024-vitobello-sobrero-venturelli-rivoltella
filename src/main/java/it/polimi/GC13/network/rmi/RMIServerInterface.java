package it.polimi.GC13.network.rmi;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Server;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    void startServer() throws RemoteException;

    void createGame(ClientInterface client) throws IOException, PlayerNotAddedException;
}
