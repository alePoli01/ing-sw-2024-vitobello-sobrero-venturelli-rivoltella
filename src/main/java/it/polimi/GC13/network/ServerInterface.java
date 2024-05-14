package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

import java.io.Serializable;

public interface ServerInterface extends Serializable{
    // Methods that can be called by the client

    void sendMessageFromClient(MessagesFromClient messages);
}
