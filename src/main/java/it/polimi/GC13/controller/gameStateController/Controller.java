package it.polimi.GC13.controller.gameStateController;


import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.GameState;
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
    //TODO: delete these 2 fields
    private final Map<Player, ClientInterface> playerClientMap = new HashMap<>();
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>();
    ControllerDispatcher controllerDispatcher;

    // add a new created game to the game manager with its respective controller
    public Controller(Game game, LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        this.game = game;
        this.lobbyController = lobbyController;
        this.controllerDispatcher = controllerDispatcher;
        this.gameController = new JoiningPhase(this);
    }

    public Controller(GameState gameState, Game game, LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        //this constructor is used when reconnecting
        //TODO: maybe we can use this when creating the game, (put gameState= null / JOINING ?)
        this.lobbyController = lobbyController;
        this.controllerDispatcher = controllerDispatcher;
        this.game = game;
        switch (gameState) {
            case SETUP -> this.gameController = new SetupPhase(this);
            case DEALING_CARDS -> this.gameController = new DealingPhase(this);
            case MID -> this.gameController = new MidPhase(this);
            case END -> this.gameController = new EndPhase(this);
            default -> this.gameController = new JoiningPhase(this);
        }

    }

    public GamePhase getGameController() {
        return gameController;
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
            System.out.println("Controller updated to " + newGameController.getClass().getSimpleName());
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
        System.out.println("Phase: "+gameController.getClass().getSimpleName());
        this.gameController.placeCard(player, cardToPlaceHandIndex, isFlipped, X, Y);
    }

    public void drawCard(Player player, int serialCardToDraw) {
        this.gameController.drawCard(player, serialCardToDraw);
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
        this.gameController.addPlayerToExistingGame(player, existingGame, client);
    }

    public void newChatMessage(String sender, String receiver, String message) {
        this.gameController.newChatMessage(sender, receiver, message);
    }
}
