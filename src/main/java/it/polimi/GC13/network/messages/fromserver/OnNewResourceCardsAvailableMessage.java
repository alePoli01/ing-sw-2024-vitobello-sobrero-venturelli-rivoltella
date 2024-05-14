package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.util.Map;
import java.rmi.RemoteException;
public record OnNewResourceCardsAvailableMessage(Map<Integer, Boolean> resourceCardSerial) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.updateResourceCardsAvailableToDraw(this.resourceCardSerial());
    }
}
