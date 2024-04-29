package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.*;

public interface ClientDispatcherInterface {

    //used by PokeMessage
    void dispatch(String message);

    void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage);

    void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);

    void dispatch(OnInputExceptionMessage onInputExceptionMessage);

    void dispatch(OnTokenChoiceMessage onTokenChoiceMessage);

    void dispatch(OnPlaceStartCardMessage onPlaceStartCardMessage);
}
