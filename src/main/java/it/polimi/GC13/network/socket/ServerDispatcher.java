package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlaceStartCardMessage;
import it.polimi.GC13.network.socket.messages.fromclient.AddPlayerToGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.TokenChoiceMessage;

public class ServerDispatcher implements ServerDispatcherInterface {
    private final ControllerDispatcher controllerDispatcher;

    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void dispatch(AddPlayerToGameMessage addPlayerToGameMessage, ClientInterface client) {
        //unpack the message and propagate to che controllerDispatcher (some messages are for the model, others are for the Lobby)
        controllerDispatcher.addPlayerToGame(client, addPlayerToGameMessage.player(), addPlayerToGameMessage.numOfPlayers(), addPlayerToGameMessage.gameName());
    }

    @Override
    public void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client) {
        controllerDispatcher.checkForExistingGame(client);
    }

    @Override
    public void dispatch(TokenChoiceMessage tokenChoiceMessage, ClientInterface client) {
        controllerDispatcher.chooseToken(client, tokenChoiceMessage.tokenColor());
    }

    @Override
    public void dispatch(PlaceStartCardMessage placeStartCardMessage, ClientInterface client) {
        controllerDispatcher.placeStartCard(client, placeStartCardMessage.getSide());
    }
}
