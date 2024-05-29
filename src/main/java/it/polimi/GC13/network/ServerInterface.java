package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.ClientDispatcher;

public interface ServerInterface {
    // Methods that can be called by the client
    void sendMessageFromClient(MessagesFromClient messages);;
    ClientDispatcher getClientDispatcher();
}
