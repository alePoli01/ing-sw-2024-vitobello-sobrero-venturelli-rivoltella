package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnPlayerAddedToGameMessage implements MessagesFromServer {
    private final int waitingPlayers;
    private final int numPlayersNeeded;
    public OnPlayerAddedToGameMessage(int waitingPlayers, int numPlayers) {
        this.waitingPlayers = waitingPlayers;
        this.numPlayersNeeded=numPlayers;
    }

    public int getNumPlayersNeeded() {
        return numPlayersNeeded;
    }

    public int getWaitingPlayers() {
        return waitingPlayers;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
