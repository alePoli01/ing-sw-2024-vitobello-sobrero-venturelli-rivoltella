package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.EnumMap;

public record OnUpdateResourceMessage(String playerNickname, EnumMap<Resource, Integer> collectedResources) implements MessagesFromServer {
    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.updateCollectedResource(this.playerNickname(), this.collectedResources());
    }
}
