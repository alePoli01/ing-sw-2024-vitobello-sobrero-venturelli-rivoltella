package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

public record OnPlayerScoreUpdateMessage(String playerNickname, int newPlayerScore ) implements MessagesFromServer {
    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {

    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.updatePlayerScore(this.playerNickname(), this.newPlayerScore());
    }
}
