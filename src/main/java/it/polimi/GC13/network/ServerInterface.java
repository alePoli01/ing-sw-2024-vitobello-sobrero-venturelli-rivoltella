package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.ClientDispatcher;

/**
 * Class that represent the server on the client.
 */
public interface ServerInterface {

    /**
     * Method used to send messages from client to the server.
     *
     * @param message message sent to the server
     */
    void sendMessageFromClient(MessagesFromClient message);

    /**
     * Method used to retrieve the client dispatcher to send the message from the server after reconnection.
     *
     * @return the clientDispatcher
     */
    ClientDispatcher getClientDispatcher();

    /**
     * Method used to signal connection between client and server.
     *
     * @param b true connection open, false connection lost
     */
    void setConnectionOpen(boolean b);
}
