package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    // Generic method to send messages from server to client after the game is started
    void sendMessageFromServer(MessagesFromServer message) throws RemoteException;

    boolean isConnectionOpen() throws RemoteException;
}