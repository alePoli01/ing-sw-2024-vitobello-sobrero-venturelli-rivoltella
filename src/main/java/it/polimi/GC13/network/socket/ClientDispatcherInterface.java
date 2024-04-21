package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerJoiningMessage;

public interface ClientDispatcherInterface {
    void dispatch(String message);//used by PokeMessage
    void dispatch(OnPlayerJoiningMessage onPlayerJoiningMessage, ServerInterface server);
}
