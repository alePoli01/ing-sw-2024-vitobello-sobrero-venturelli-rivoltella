package it.polimi.GC13.network.socket.messages.fromserver;


import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

public record OnDealPrivateObjectiveCardsMessage(String playerNickname, int[] privateObjectiveCards) implements MessagesFromServer {

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.chosePrivateObjectiveCard(this.playerNickname, this.privateObjectiveCards);
    }
}
