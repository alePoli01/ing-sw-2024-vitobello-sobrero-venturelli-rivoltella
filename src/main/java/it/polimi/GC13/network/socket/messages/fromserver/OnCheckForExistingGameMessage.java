package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

import java.util.Map;

public record OnCheckForExistingGameMessage(Map<String, Integer> gameNameWaitingPlayersMap) implements MessagesFromServer {

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }
}
