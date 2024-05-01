package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.*;

public class ServerDispatcher implements ServerDispatcherInterface {
    private final ControllerDispatcher controllerDispatcher;

    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void dispatch(AddPlayerToGameMessage addPlayerToGameMessage, ClientInterface client) {
        //unpack the message and propagate to che controllerDispatcher (some messages are for the model, others are for the Lobby)
        this.controllerDispatcher.addPlayerToGame(client, addPlayerToGameMessage.player(), addPlayerToGameMessage.numOfPlayers(), addPlayerToGameMessage.gameName());
    }

    @Override
    public void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client) {
        this.controllerDispatcher.checkForExistingGame(client);
    }

    @Override
    public void dispatch(TokenChoiceMessage tokenChoiceMessage, ClientInterface client) {
        this.controllerDispatcher.chooseToken(client, tokenChoiceMessage.tokenColor());
    }

    @Override
    public void dispatch(PlaceStartCardMessage placeStartCardMessage, ClientInterface client) {
        this.controllerDispatcher.placeStartCard(client, placeStartCardMessage.getSide());
    }

    @Override
    public void dispatch(ChoosePrivateObjectiveCardMessage choosePrivateObjectiveCardMessage, ClientInterface client) {
        this.controllerDispatcher.choosePrivateObjective(client, choosePrivateObjectiveCardMessage.indexPrivateObjectiveCard());
    }
}
