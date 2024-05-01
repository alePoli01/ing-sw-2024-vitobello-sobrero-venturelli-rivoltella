package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

import java.io.Serializable;

public interface MessagesFromServer extends Serializable {
    //void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface view);
    void dispatch(ClientDispatcherInterface clientDispatcher);

    // send message to the clientInterface (socket / RMI client)
    void notifyClient(ClientInterface client);
}
