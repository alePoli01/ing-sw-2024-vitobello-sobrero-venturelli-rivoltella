package it.polimi.GC13.network.rmi;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * RMIServerInterface represents the interface that defines methods callable remotely by RMI clients.
 * <p>
 * Extends the {@link Remote} interface. It declares methods for handling messages from clients and creating
 * connections with clients.
 */
public interface RMIServerInterface extends Remote {

    /**
     * Registers a message from a client to be processed by the server.
     *
     * @param message The message received from the client.
     * @param client  The client interface associated with the message.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    void registerMessageFromClient(MessagesFromClient message, ClientInterface client) throws RemoteException;


    /**
     * Creates a connection with a client.
     *
     * @param client The client interface representing the client to connect with.
     * @throws RemoteException If a communication-related exception occurs during the remote method call.
     */
    void createConnection(ClientInterface client) throws RemoteException;
}
