package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnPlayerAddedToGameMessage implements MessagesFromServer {

    public OnPlayerAddedToGameMessage() {
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher, ServerInterface server) {
        clientDispatcher.dipatch(this);
    }
}
