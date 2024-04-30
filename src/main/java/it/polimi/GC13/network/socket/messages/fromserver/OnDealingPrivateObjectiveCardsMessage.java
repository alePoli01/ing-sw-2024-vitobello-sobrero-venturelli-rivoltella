package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public record OnDealingPrivateObjectiveCardsMessage(int[] privateObjectiveCards) implements MessagesFromServer {

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
