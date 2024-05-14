package it.polimi.GC13.network.rmi;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    void registerMessageFromClient(MessagesFromClient message, ClientInterface client) throws RemoteException;
}
