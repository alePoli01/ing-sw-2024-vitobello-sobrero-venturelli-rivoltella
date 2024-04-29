package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

public class PlaceStartCardMessage implements MessagesFromClient {
    private final boolean side;

    public PlaceStartCardMessage(boolean isFlipped) {
        this.side = isFlipped;
    }

    public boolean getSide() {
        return this.side;
    }

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException, PlayerNotAddedException {
        serverDispatcher.dispatch(this, client);
    }
}
