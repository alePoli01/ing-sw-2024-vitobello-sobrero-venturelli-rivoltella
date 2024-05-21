package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.ClientDispatcher;

import java.io.Serializable;

public interface ServerInterface extends Serializable{
    // Methods that can be called by the client
    void sendMessageFromClient(MessagesFromClient messages);
    boolean isConnectionOpen();
    ClientDispatcher getClientDispatcher();
}
