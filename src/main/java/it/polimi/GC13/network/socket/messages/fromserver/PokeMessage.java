package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class PokeMessage implements MessagesFromServer {
    private final String message;

    public PokeMessage() {
        this.message = null;
    }

    public PokeMessage(String message) {
        this.message = message;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(message);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }
}
