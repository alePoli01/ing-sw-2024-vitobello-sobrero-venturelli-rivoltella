package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;
import java.io.IOException;

public class TokenChoiceMessage implements MessagesFromClient {
    public final TokenColor tokenColor;

    public TokenChoiceMessage(TokenColor tokenColor) {
        this.tokenColor = tokenColor;
    }

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {

    }
}
