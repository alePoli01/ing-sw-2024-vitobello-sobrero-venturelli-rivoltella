package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.*;

public interface ServerDispatcherInterface {

    void dispatch(AddPlayerToGameMessage addPlayerToGameMessage, ClientInterface client);

    void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client);

    void dispatch(TokenChoiceMessage tokenChoiceMessage, ClientInterface client);

    void dispatch(PlaceStartCardMessage placeStartCardMessage, ClientInterface client);

    void dispatch(ChoosePrivateObjectiveCardMessage choosePrivateObjectiveCardMessage, ClientInterface client);

    void dispatch(PlaceCardMessage placeCardMessage, ClientInterface client);
}
