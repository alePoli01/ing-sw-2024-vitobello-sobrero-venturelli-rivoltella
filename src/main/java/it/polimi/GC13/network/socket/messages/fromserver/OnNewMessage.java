package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public record OnNewMessage(String sender, String receiver, String message) implements MessagesFromServer {

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {

    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.onNewMessage(this.sender(), this.receiver(), this.message());
    }
}

