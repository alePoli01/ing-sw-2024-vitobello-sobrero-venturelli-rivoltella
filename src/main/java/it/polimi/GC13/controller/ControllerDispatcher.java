package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.ControllerInterface;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.exception.inputException.TokenAlreadyChosenException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerDispatcher implements LobbyControllerInterface, ControllerInterface {
    private final LobbyController lobbyController;
    private final Map<ClientInterface, Controller> clientControllerMap = new ConcurrentHashMap<>();
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>();

    public ControllerDispatcher(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    public Map<ClientInterface, Controller> getClientControllerMap() {
        return clientControllerMap;
    }

    public Map<ClientInterface, Player> getClientPlayerMap() {
        return clientPlayerMap;
    }

    @Override
    public void addPlayerToGame(ClientInterface view, Player player, int playersNumber, String gameName) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {
        lobbyController.addPlayerToGame(view, player, playersNumber, gameName);
    }

    @Override
    public void checkForExistingGame(ClientInterface client) {
        lobbyController.checkForExistingGame(client);
    }

    @Override
    public void chooseToken(ClientInterface client, TokenColor tokenColor) {
        try {
            this.clientControllerMap.get(client).chooseToken(this.clientPlayerMap.get(client), tokenColor);
            client.onTokenChoiceMessage(tokenColor);
        } catch (TokenAlreadyChosenException e) {
            client.inputExceptionHandler(e);
        }
    }

    @Override
    public void choosePrivateObjective(ClientInterface client, Player player, ObjectiveCard card) {
        this.clientControllerMap.get(client).choosePrivateObjective(player, card);
    }

    @Override
    public void placeStartCard(ClientInterface client, Player player, StartCard cardToPlace, boolean isFlipped) {
        this.clientControllerMap.get(client).placeStartCard(player, cardToPlace, isFlipped);
    }

    @Override
    public void placeCard(ClientInterface client, Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.clientControllerMap.get(client).placeCard(player, cardToPlace, isFlipped, xy);
    }

    @Override
    public void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw) {
        this.clientControllerMap.get(client).drawCard(player, table, cardToDraw);
    }

    @Override
    public boolean addPlayerToExistingGame(ClientInterface client, Player player, Game existingGame) throws PlayerNotAddedException {
        return false;
    }
}
