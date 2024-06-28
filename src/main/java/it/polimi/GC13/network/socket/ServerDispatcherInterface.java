package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

/**
 * Interface for dispatching messages received from clients to the controller dispatcher.
 */
public interface ServerDispatcherInterface {

    /**
     * Sends messages from a client to the controller dispatcher for further processing.
     *
     * @param messagesFromClient The messages received from the client.
     * @param client             The client interface representing the sender of the messages.
     */
    void sendToControllerDispatcher(MessagesFromClient messagesFromClient, ClientInterface client) ;
}
