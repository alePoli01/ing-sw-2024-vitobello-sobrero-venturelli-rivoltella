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

/**
 * Class that represents the controller which is responsible to call the correct game phase class to perform the action request by the players
 */
public class Controller implements GamePhase {
    /**
     * The current game phase controller instance.
     */
    private GamePhase gameController;


    /**
     * The {@link Game} instance associated with this controller.
     */
    private final Game game;

    /**
     * The {@link LobbyController} instance managing the lobby.
     */
    private final LobbyController lobbyController;

    /**
     * Mapping of Player to ClientInterface for active players.
     */
    private final Map<Player, ClientInterface> playerClientMap = new HashMap<>();

    /**
     * Mapping of ClientInterface to Player for active clients.
     */
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>();

    /**
     * Dispatcher for managing controllers and game states.
     */
    ControllerDispatcher controllerDispatcher;



    /**
     * Constructs a {@code Controller} instance for a new game.
     *
     * @param game               The Game instance associated with this controller.
     * @param lobbyController    The LobbyController instance managing the lobby.
     * @param controllerDispatcher The ControllerDispatcher instance managing game states.
     */
    public Controller(Game game, LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        this.game = game;
        this.lobbyController = lobbyController;
        this.controllerDispatcher = controllerDispatcher;
        this.gameController = new JoiningPhase(this);
    }


    /**
     * Constructs a {@code Controller} instance for an existing game with a specific GameState.
     *
     * @param gameState          The current GameState of the game.
     * @param game               The Game instance associated with this controller.
     * @param lobbyController    The LobbyController instance managing the lobby.
     * @param controllerDispatcher The ControllerDispatcher instance managing game states.
     */
    public Controller(GameState gameState, Game game, LobbyController lobbyController, ControllerDispatcher controllerDispatcher) {
        //this constructor is used when reconnecting
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
    /**
     * Retrieves the current game phase controller.
     *
     * @return The current game phase controller instance.
     */
    public GamePhase getGameController() {
        return gameController;
    }

    /**
     * Retrieves the LobbyController instance associated with this game.
     *
     * @return The LobbyController instance.
     */
    public LobbyController getLobbyController() {
        return this.lobbyController;
    }

    /**
     * Retrieves the Game instance associated with this controller.
     *
     * @return The Game instance.
     */
    public Game getGame() {
        return game;
    }

    /**
     * Retrieves the mapping of ClientInterface to Player for active clients.
     *
     * @return The mapping of ClientInterface to Player.
     */
    public Map<ClientInterface, Player> getClientPlayerMap() {
        return this.clientPlayerMap;
    }

    public Map<Player, ClientInterface> getPlayerClientMap() {
        return this.playerClientMap;
    }


    /**
     * Adds a Player-ClientInterface pair to the mapping.
     *
     * @param player The Player instance.
     * @param client The ClientInterface instance.
     */
    public void addToMaps(Player player, ClientInterface client) {
        this.clientPlayerMap.put(client, player);
        this.playerClientMap.put(player, client);
    }

    /**
     * Removes a Player from the mapping.
     *
     * @param player The Player instance.
     */
    public void removeFromMaps(Player player) {
        this.clientPlayerMap.remove(playerClientMap.get(player));
        this.playerClientMap.remove(player);
    }


    /**
     * Updates the current game phase controller to a new one.
     *
     * @param newGameController The new GamePhase instance to set.
     */
    public void updateController(GamePhase newGameController) {
        synchronized (this) {
            this.gameController = newGameController;
            System.out.println("Controller updated to " + newGameController.getClass().getSimpleName());
        }
    }


    @Override
    public void chooseToken(Player player, TokenColor token) {
        this.gameController.chooseToken(player, token);
    }


    @Override
    public void choosePrivateObjective(Player player, int serialPrivateObjectiveCard) {
        this.gameController.choosePrivateObjective(player, serialPrivateObjectiveCard);
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        this.gameController.placeStartCard(player, isFlipped);
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        this.gameController.placeCard(player, cardToPlaceHandIndex, isFlipped, X, Y);
    }

    @Override
    public void drawCard(Player player, int serialCardToDraw) {
        this.gameController.drawCard(player, serialCardToDraw);
    }

    @Override
    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
        this.gameController.addPlayerToExistingGame(player, existingGame, client);
    }

    @Override
    public void newChatMessage(String sender, String receiver, String message) {
        this.gameController.newChatMessage(sender, receiver, message);
    }

    /**
     * Closes the game for a specific client, removing them from the game and updating mappings.
     *
     * @param client The client interface of the player to close the game for.
     */
    public void closeGame(ClientInterface client) {
        this.game.closeGame(client, this.clientPlayerMap.get(client).getNickname());
        this.game.getPlayerList().forEach(this::removeFromMaps);
    }
}
