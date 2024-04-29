package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

public class PlaceStartCardMessage implements MessagesFromClient {
    private final boolean side;

    public PlaceStartCardMessage(boolean side) {
        this.side = side;
    }

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException, PlayerNotAddedException {

    }
}
