package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

public record CheckForExistingGameMessage() implements MessagesFromClient {
    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException {
        serverDispatcher.dispatch(this, client);
    }
}
