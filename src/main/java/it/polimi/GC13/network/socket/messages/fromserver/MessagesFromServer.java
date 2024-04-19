package it.polimi.GC13.network.socket.messages.fromserver;

import java.io.Serializable;

public interface MessagesFromServer extends Serializable {
    //void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface view);
    void dispatch(/* clientDispatcher*/);
}
