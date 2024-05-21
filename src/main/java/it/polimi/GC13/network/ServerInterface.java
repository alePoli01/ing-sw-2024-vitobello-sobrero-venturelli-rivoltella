package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

import java.io.Serializable;

public interface ServerInterface extends Serializable{
    // Methods that can be called by the client

    void sendMessageFromClient(MessagesFromClient messages);
    String getGameName();
    String getPlayerName();
    boolean isConnectionOpen();
   ClientDispatcherInterface getClientDispatcher();
}
