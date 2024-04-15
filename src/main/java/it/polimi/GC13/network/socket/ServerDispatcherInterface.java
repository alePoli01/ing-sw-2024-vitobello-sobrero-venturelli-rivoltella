package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

public interface ServerDispatcherInterface {
    void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface view);//TODO: da capire il secondo paametro
}
