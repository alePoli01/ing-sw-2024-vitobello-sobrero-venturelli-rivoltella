package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.*;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;

public interface ClientDispatcherInterface {

    //used by PokeMessage
    void dispatch(String message);

    void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage);

    void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);

    void dispatch(OnInputExceptionMessage onInputExceptionMessage);

    void dispatch(OnTokenChoiceMessage onTokenChoiceMessage);

    void dispatch(OnPlaceStartCardMessage onPlaceStartCardMessage);

    void dispatch(OnDealCardMessage onDealCardMessage);

    void dispatch(OnDealPrivateObjectiveCardsMessage onDealPrivateObjectiveCardsMessage);

    void dispatch(OnDealCommonObjectiveCardMessage onDealCommonObjectiveCardMessage);
}
