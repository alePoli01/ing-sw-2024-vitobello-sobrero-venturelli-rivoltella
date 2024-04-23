package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

import java.io.IOException;

public interface ServerDispatcherInterface {

    void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface client) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException;

    void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client);
}
