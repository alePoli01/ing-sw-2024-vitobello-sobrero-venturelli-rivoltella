package it.polimi.GC13.controller.gameStateController;


import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.exception.inputException.TokenAlreadyChosenException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnDealingCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceStartCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Controller implements GamePhase {
    private GamePhase gameController;
    private final Game game;
    private LobbyController lobbyController;
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
        this.gameController = newGameController;
        System.out.println("Controller updated to " + newGameController.getClass());
    }

    public void notifyClients(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage) {
        this.playerClientMap.forEach((player, client) -> client.onPlayerAddedToGame(onPlayerAddedToGameMessage));
    }

    public void notifySpecificClients(OnDealingCardMessage onDealingCardMessage, List<Player> playerList) {
        playerList.stream()
                .map(this.playerClientMap::get)
                .forEach(client -> client.onDealingCard(onDealingCardMessage.availableCards()));
    }

    public void notifySpecificClients(OnPlaceStartCardMessage onPlaceStartCardMessage, List<Player> playerList) {
        playerList.stream()
                .map(this.playerClientMap::get)
                .forEach(client -> client.onPlaceStartCardMessage(onPlaceStartCardMessage));
    }


    public void chooseToken(Player player, TokenColor token) throws TokenAlreadyChosenException {
        this.gameController.chooseToken(player, token);
    }

    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        this.gameController.choosePrivateObjective(player, card);
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        this.gameController.placeStartCard(player, isFlipped);
    }

    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.gameController.placeCard(player, cardToPlace, isFlipped, xy);
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        this.gameController.drawCard(player, table, cardToDraw);
    }

    public void addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException, NicknameAlreadyTakenException {
        this.gameController.addPlayerToExistingGame(player, existingGame);
    }
}
