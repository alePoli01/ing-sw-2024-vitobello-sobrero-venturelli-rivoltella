package it.polimi.GC13.controller.gameStateController;


import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.util.HashMap;
import java.util.Map;

public class Controller implements GamePhase {
    private GamePhase gameController;
    private final Game game;
    private final LobbyController lobbyController;
    private final Map<Player, ClientInterface> playerClientMap = new HashMap<>();
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>();
    ControllerDispatcher controllerDispatcher;

    // add a new created game to the game manager with its respective controller
    public Controller(Game game, LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        this.gameController = new JoiningPhase(this);
        this.game = game;
        this.lobbyController = lobbyController;
        this.controllerDispatcher = controllerDispatcher;
    }

    public LobbyController getLobbyController() {
        return this.lobbyController;
    }

    public Game getGame() {
        return game;
    }

    public Map<ClientInterface, Player> getClientPlayerMap() {
        return this.clientPlayerMap;
    }

    public Map<Player, ClientInterface> getPlayerClientMap() {
        return this.playerClientMap;
    }

    public void updateController(GamePhase newGameController) {
        synchronized (this) {
            this.gameController = newGameController;
            System.out.println("Controller updated to " + newGameController.getClass());
        }
    }

    public void chooseToken(Player player, TokenColor token) {
        this.gameController.chooseToken(player, token);
    }

    public void choosePrivateObjective(Player player, int serialPrivateObjectiveCard) {
        this.gameController.choosePrivateObjective(player, serialPrivateObjectiveCard);
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        this.gameController.placeStartCard(player, isFlipped);
    }

    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        this.gameController.placeCard(player, cardToPlaceHandIndex, isFlipped, X, Y);
    }

    public void drawCard(Player player, int cardDeckIndex) {
        this.gameController.drawCard(player, cardDeckIndex);
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
        this.gameController.addPlayerToExistingGame(player, existingGame, client);
    }
}
