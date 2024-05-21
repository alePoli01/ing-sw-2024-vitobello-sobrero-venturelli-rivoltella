package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

public interface ClientDispatcherInterface {
    void registerMessageFromServer(MessagesFromServer message);
}
