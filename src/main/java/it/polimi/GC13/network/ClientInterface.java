package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Class that represent the client on the server
 */
public interface ClientInterface extends Remote {

    /**
     * Method to send messages from server to client after the game is started
     * @param message message sent
     * @throws RemoteException thrown if RMI connection is interrupted
     */
    void sendMessageFromServer(MessagesFromServer message) throws RemoteException;

    /**
     * method used to check if the connection with the client
     * @return true for connection open, false if it is closed
     * @throws RemoteException thrown if RMI connection is interrupted
     */
    boolean isConnectionOpen() throws RemoteException;
}