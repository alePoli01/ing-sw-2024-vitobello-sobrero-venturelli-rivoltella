package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;
import it.polimi.GC13.network.socket.messages.fromclient.TokenChoiceMessage;

import java.io.IOException;

public interface ServerDispatcherInterface {

    void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface client) throws IOException, PlayerNotAddedException ;

    void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client);

    void dispatch(TokenChoiceMessage tokenChoiceMessage, ClientInterface client);
}
