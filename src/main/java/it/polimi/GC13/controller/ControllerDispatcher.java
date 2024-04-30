package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.ControllerInterface;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
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
    public void addPlayerToGame(ClientInterface client, Player player, int playersNumber, String gameName) throws IOException, PlayerNotAddedException {
        try {
            lobbyController.addPlayerToGame(client, player, playersNumber, gameName);
        } catch (PlayerNotAddedException e) {
            System.out.println("Sto per lanciare eccezione");
            client.inputExceptionHandler(e);
        }
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
    public void placeStartCard(ClientInterface client, boolean isFlipped) {
        this.clientControllerMap.get(client).placeStartCard(this.clientPlayerMap.get(client), isFlipped);
    }

    @Override
    public void choosePrivateObjective(ClientInterface client, ObjectiveCard card) {
        // da fixare il cast, l'ho messo per non ricevere errore, quando lo fai sistema -Ale
        this.clientControllerMap.get(client).choosePrivateObjective((Player) client, card);
    }


    @Override
    public void placeCard(ClientInterface client, Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.clientControllerMap.get(client).placeCard(player, cardToPlace, isFlipped, xy);
    }

    @Override
    public void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw) {
        this.clientControllerMap.get(client).drawCard(player, table, cardToDraw);
    }


}
