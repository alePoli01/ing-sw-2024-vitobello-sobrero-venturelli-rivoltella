package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.Map;

public record OnTurnSetMessage(Map<String, Position> playerPositions) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.setPlayersOrder(this.playerPositions);
    }
}
