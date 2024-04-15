package it.polimi.GC13.network.socket.messages;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

public interface ServerMessage {

    void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client);
}
