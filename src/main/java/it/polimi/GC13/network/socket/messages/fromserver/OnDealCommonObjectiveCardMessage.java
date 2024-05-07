package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.util.List;

public record OnDealCommonObjectiveCardMessage(List<Integer> privateObjectiveCards) implements MessagesFromServer {

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);    }

    @Override
    public void methodToCall(View view) {
        view.setSerialCommonObjectiveCard(this.privateObjectiveCards());
    }
}
