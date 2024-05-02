package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.ControllerInterface;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

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
    public void addPlayerToGame(ClientInterface client, Player player, int playersNumber, String gameName) {
        lobbyController.addPlayerToGame(client, player, playersNumber, gameName);
    }

    @Override
    public void checkForExistingGame(ClientInterface client) {
        lobbyController.checkForExistingGame(client);
    }

    @Override
    public void chooseToken(ClientInterface client, TokenColor tokenColor) {
        this.clientControllerMap.get(client).chooseToken(this.clientPlayerMap.get(client), tokenColor);
    }

    @Override
    public void placeStartCard(ClientInterface client, boolean isFlipped) {
        this.clientControllerMap.get(client).placeStartCard(this.clientPlayerMap.get(client), isFlipped);
    }

    @Override
    public void choosePrivateObjective(ClientInterface client, int privateObjectiveCardIndex) {
        this.clientControllerMap.get(client).choosePrivateObjective(this.clientPlayerMap.get(client), privateObjectiveCardIndex);
    }


    @Override
    public void placeCard(ClientInterface client, int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy) {
        this.clientControllerMap.get(client).placeCard(this.clientPlayerMap.get(client), cardToPlaceHandIndex, isFlipped, xy);
    }

    @Override
    public void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw) {
        this.clientControllerMap.get(client).drawCard(player, table, cardToDraw);
    }
}
