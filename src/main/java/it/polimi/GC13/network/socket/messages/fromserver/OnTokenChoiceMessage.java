package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public record OnTokenChoiceMessage(TokenColor tokenColor) implements MessagesFromServer {

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
