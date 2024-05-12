package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MessagesFromServer extends Serializable {

    // send message to the clientInterface (socket / RMI client)
    void notifyClient(ClientInterface client) throws RemoteException;

    // calls the correct method on the TUI
    void methodToCall(View view) ;
}
