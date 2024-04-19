package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

import java.io.IOException;

public interface ServerDispatcherInterface {
    void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface view) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException;
}
