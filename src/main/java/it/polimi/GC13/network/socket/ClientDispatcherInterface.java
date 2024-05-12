package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.*;

public interface ClientDispatcherInterface {

    void registerMessageFromServer(MessagesFromServer message);
}
