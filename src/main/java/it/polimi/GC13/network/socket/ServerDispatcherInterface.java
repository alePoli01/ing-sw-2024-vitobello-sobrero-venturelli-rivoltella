package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

public interface ServerDispatcherInterface {
    void sendToControllerDispatcher(MessagesFromClient messagesFromClient, ClientInterface client) ;
}
