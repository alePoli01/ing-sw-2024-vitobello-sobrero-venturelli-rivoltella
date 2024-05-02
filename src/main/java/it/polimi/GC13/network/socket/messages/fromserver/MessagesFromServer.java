package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.io.Serializable;
import java.util.concurrent.Delayed;

public interface MessagesFromServer extends Serializable {
    //void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface view);
    void dispatch(ClientDispatcherInterface clientDispatcher);

    // send message to the clientInterface (socket / RMI client)
    void notifyClient(ClientInterface client);

    // calls the correct method on the TUI
    void methodToCall(View view);
}
