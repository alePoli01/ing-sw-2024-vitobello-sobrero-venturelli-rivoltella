package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.io.Serializable;

public interface MessagesFromServer extends Serializable {

    // method to handle poke message
    void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher);

    // send message to the clientInterface (socket / RMI client)
    void notifyClient(ClientInterface client);

    // calls the correct method on the TUI
    void methodToCall(View view);
}
