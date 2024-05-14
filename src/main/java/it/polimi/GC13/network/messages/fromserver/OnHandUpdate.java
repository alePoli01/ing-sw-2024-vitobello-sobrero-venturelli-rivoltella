package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.LinkedList;

public record OnHandUpdate(String playerNickname, LinkedList<Integer> availableCards) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
            client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.handUpdate(this.playerNickname, this.availableCards());
    }
}