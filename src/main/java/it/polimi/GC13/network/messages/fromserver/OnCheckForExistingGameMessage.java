package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.Map;

public record OnCheckForExistingGameMessage(Map<String, Integer> gameNameWaitingPlayersMap) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        try {
            view.joiningPhase(this.gameNameWaitingPlayersMap);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }
}