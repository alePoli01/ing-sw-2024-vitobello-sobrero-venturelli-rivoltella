package it.polimi.GC13.network.rmi;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public class RMIClientImpl implements Remote, Serializable, ClientInterface {
    private final ClientDispatcherInterface clientDispatcher;

    public RMIClientImpl(ClientDispatcherInterface clientDispatcher) {
        //this.view = view;
        this.clientDispatcher = clientDispatcher;
    }

    @Override
    public void sendMessage(MessagesFromServer message) throws RemoteException {
        //message.methodToCall(this.view);
    }

    @Override
    public void poke() throws IOException {

    }
}
