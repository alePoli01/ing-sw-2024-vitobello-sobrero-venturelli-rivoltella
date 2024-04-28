package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnInputExceptionMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnTokenChoiceMessage;

public interface ClientDispatcherInterface {

    //used by PokeMessage
    void dispatch(String message);

    void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage);

    void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);

    void dispatch(OnInputExceptionMessage onInputExceptionMessage);

    void dispatch(OnTokenChoiceMessage onTokenChoiceMessage);
}
