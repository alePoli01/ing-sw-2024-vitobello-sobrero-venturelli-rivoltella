package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public record OnDealingCardMessage(int[] availableCards) implements MessagesFromServer {

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}