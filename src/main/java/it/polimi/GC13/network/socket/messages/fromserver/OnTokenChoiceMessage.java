package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnTokenChoiceMessage implements MessagesFromServer {
    private final TokenColor tokenColor;

    public OnTokenChoiceMessage(TokenColor tokenColor) {
        this.tokenColor = tokenColor;
    }

    public TokenColor getTokenColor() {
        return tokenColor;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
