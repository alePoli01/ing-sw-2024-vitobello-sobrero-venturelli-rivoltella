package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

public interface MessagesFromClient {

    void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException;
}
