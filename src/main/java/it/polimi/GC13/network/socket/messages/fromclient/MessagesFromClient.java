package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;
import java.io.Serializable;

public interface MessagesFromClient extends Serializable {

    void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException;
}
