package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnCheckForExistingGameMessage implements MessagesFromServer {
    private int waitingPlayers;

    public OnCheckForExistingGameMessage(int noExistingGames) {
        this.waitingPlayers = noExistingGames;
    }

    public int getWaitingPlayers() {
        return this.waitingPlayers;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher, ServerInterface server) {
        clientDispatcher.dispatch(this, server);
    }
}
