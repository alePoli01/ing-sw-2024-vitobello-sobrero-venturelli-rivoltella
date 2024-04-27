package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.LobbyControllerInterface;
import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.ControllerInterface;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerDispatcher implements LobbyControllerInterface, ControllerInterface {
    private final LobbyController lobbyController;
    private final Map<ClientInterface, Controller> clientGameMap = new ConcurrentHashMap<>();

    public ControllerDispatcher(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    public Map<ClientInterface, Controller> getClientGameMap() {
        return clientGameMap;
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
    public void chooseToken(ClientInterface client, Player player, TokenColor token) {
        this.clientGameMap.get(client).chooseToken(player, token);
    }

    @Override
    public void choosePrivateObjective(ClientInterface client, Player player, ObjectiveCard card) {
        this.clientGameMap.get(client).choosePrivateObjective(player, card);
    }

    @Override
    public void placeStartCard(ClientInterface client, Player player, StartCard cardToPlace, boolean isFlipped) {
        this.clientGameMap.get(client).placeStartCard(player, cardToPlace, isFlipped);
    }

    @Override
    public void placeCard(ClientInterface client, Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.clientGameMap.get(client).placeCard(player, cardToPlace, isFlipped, xy);
    }

    @Override
    public void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw) {
        this.clientGameMap.get(client).drawCard(player, table, cardToDraw);
    }

    @Override
    public boolean addPlayerToExistingGame(ClientInterface client, Player player, Game existingGame) throws PlayerNotAddedException {
        return false;
    }
}
