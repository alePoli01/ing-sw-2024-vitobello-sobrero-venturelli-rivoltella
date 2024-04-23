package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

public class OnCheckForExistingGameMessage implements MessagesFromServer {
    private final Boolean noExistingGames;

    public OnCheckForExistingGameMessage(Boolean noExistingGames) {
        this.noExistingGames = noExistingGames;
    }

    public Boolean getNoExistingGames() {
        return noExistingGames;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher, ServerInterface server) {
        clientDispatcher.dispatch(this, server);
    }
}
