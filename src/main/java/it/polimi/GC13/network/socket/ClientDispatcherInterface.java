package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

public interface ClientDispatcherInterface {
    void dispatch(String message);//used by PokeMessage

    void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage, ServerInterface server);

    void dipatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);
}
