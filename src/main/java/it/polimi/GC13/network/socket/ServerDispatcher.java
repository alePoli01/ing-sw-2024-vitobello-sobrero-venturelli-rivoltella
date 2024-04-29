package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlaceStartCardMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;
import it.polimi.GC13.network.socket.messages.fromclient.TokenChoiceMessage;

import java.io.IOException;

public class ServerDispatcher implements ServerDispatcherInterface {
    private final ControllerDispatcher controllerDispatcher;

    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface client) throws IOException, PlayerNotAddedException{
        //unpack the message and propagate to che controllerDispatcher (some messages are for the model, others are for the Lobby)
        controllerDispatcher.addPlayerToGame(client, playerJoiningMessage.getPlayer(), playerJoiningMessage.getNumOfPlayers(), playerJoiningMessage.getGameName());
    }

    @Override
    public void dispatch(CheckForExistingGameMessage checkForExistingGameMessage, ClientInterface client) {
        controllerDispatcher.checkForExistingGame(client);
    }

    @Override
    public void dispatch(TokenChoiceMessage tokenChoiceMessage, ClientInterface client) {
        controllerDispatcher.chooseToken(client, tokenChoiceMessage.getTokenColor());
    }

    @Override
    public void dispatch(PlaceStartCardMessage placeStartCardMessage, ClientInterface client) {
        controllerDispatcher.placeStartCard(client, placeStartCardMessage.getSide());
    }
}
