package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnExceptionMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

public interface ClientDispatcherInterface {

    //used by PokeMessage
    void dispatch(String message);

    void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage);

    void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);

    void dispatch(OnExceptionMessage onExceptionMessage);
}
