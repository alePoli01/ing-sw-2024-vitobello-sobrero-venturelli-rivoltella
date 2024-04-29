package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnPlayerAddedToGameMessage implements MessagesFromServer {
    private final int connectedPlayers;
    private final int numPlayersNeeded;

    public OnPlayerAddedToGameMessage(int connectedPlayers, int numPlayersNeeded) {
        this.connectedPlayers = connectedPlayers;
        this.numPlayersNeeded = numPlayersNeeded;
    }

    public int getNumPlayersNeeded() {
        return numPlayersNeeded;
    }

    public int getConnectedPlayers() {
        return connectedPlayers;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
