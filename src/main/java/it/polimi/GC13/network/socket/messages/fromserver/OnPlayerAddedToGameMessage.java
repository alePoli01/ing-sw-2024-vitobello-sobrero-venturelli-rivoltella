package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnPlayerAddedToGameMessage implements MessagesFromServer {
    int waitingPlayers;

    public OnPlayerAddedToGameMessage(int waitingPlayers) {
        this.waitingPlayers = waitingPlayers;
    }

    public int getWaitingPlayers() {
        return waitingPlayers;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dipatch(this);
    }
}
