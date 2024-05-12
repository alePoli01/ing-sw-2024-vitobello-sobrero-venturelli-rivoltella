package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.Map;

public record OnCheckForExistingGameMessage(Map<String, Integer> gameNameWaitingPlayersMap) implements MessagesFromServer {

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.joiningPhase(this.gameNameWaitingPlayersMap);
    }
}
