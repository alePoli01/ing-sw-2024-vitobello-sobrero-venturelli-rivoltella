package it.polimi.GC13.controller;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromserver.OnReconnectPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnPlayerNotReconnectedMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnGameNameAlreadyTakenMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnNickNameAlreadyTakenMessage;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class LobbyController implements Serializable {
    private final Map<String, Game> joinableGameMap = new ConcurrentHashMap<>();
    private final Map<String, Game> startedGameMap = new ConcurrentHashMap<>();
    private transient final Map<Game, Controller> gameControllerMap = new ConcurrentHashMap<>(); //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public Map<Game, Controller> getGameControllerMap() {
        return this.gameControllerMap;
    }

    public Map<String, Game> getJoinableGameMap() {
        return this.joinableGameMap;
    }

    public Map<String, Game> getStartedGameMap() {
        return this.startedGameMap;
    }

    public void checkForExistingGame(ClientInterface client) throws RemoteException {
        //upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        Map<String, Integer> gameNameWaitingPlayersMap = new ConcurrentHashMap<>();
        this.joinableGameMap.forEach((gameName, game) -> gameNameWaitingPlayersMap.put(gameName, game.getCurrNumPlayer()));
        client.sendMessageFromServer(new OnCheckForExistingGameMessage(gameNameWaitingPlayersMap));
    }

    /**
        METHOD TO ADD EACH PLAYER TO THE GAME
     */
    public synchronized void addPlayerToGame(ClientInterface client, String playerNickname, String gameName) throws RemoteException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + playerNickname+"]");
        try {
            // creates the player
            Player player = new Player(playerNickname);
            Game workingGame = joinableGameMap.get(gameName);
            // updates controller playerClientMap
            this.gameControllerMap.get(workingGame).getPlayerClientMap().put(player, client);
            // updates controller clientPlayerMap
            this.gameControllerMap.get(workingGame).getClientPlayerMap().put(client, player);
            // set the game for the player
            player.setGame(workingGame);
            // adds the player to the model
            this.gameControllerMap.get(workingGame).addPlayerToExistingGame(player, workingGame, client);
            // updates controller dispatcher client <-> player Map
            this.controllerDispatcher.getClientPlayerMap().put(client, player);
            // updates Controller Dispatcher's ClientGameMap adding <client, gamePhase>
            this.controllerDispatcher.getClientControllerMap().put(client, this.gameControllerMap.get(workingGame));
        } catch (GenericException e) {
            client.sendMessageFromServer(new OnNickNameAlreadyTakenMessage(playerNickname));
            System.err.println(e.getMessage());
        }
    }

    /**
        METHOD TO CREATE A NEW GAME
     */
    public synchronized void createNewGame(ClientInterface client, String playerNickname, int playersNumber, String gameName) throws RemoteException {
        Game workingGame;
        if (this.startedGameMap.containsKey(gameName) | this.joinableGameMap.containsKey(gameName)) {
            client.sendMessageFromServer(new OnGameNameAlreadyTakenMessage(playerNickname, gameName));
        } else {
            workingGame = new Game(playersNumber, gameName);
            // updates useful maps
            this.gameControllerMap.put(workingGame, new Controller(workingGame, this, this.controllerDispatcher));
            this.joinableGameMap.put(gameName, workingGame);
            this.addPlayerToGame(client, playerNickname, gameName);
        }
    }

    public synchronized void reconnectPlayerToGame(ClientInterface client, String gameName, String playerName) throws RemoteException {
        System.out.println("--Received: reconnectPlayerToGame");

        if (this.restartGames(gameName, playerName, client)) {
            System.out.println("Game and Player name was found, reconnecting client");
            client.sendMessageFromServer(new OnReconnectPlayerToGameMessage());
        } else {
            System.err.println("player: " + playerName + " or game:  " + gameName + " were not found in game.");
            client.sendMessageFromServer(new OnPlayerNotReconnectedMessage(playerName));
        }
    }

    public boolean restartGames(String gameName, String playerName, ClientInterface client) throws RemoteException {
        DiskManager diskManager = new DiskManager();
        Game game = diskManager.readFromDisk(gameName);

        if (game != null) {
            System.out.println("Found serialized file: " + game.getGameName());
            if (game.getPlayerList().stream().anyMatch(player -> player.getNickname().equals(playerName))) {
                this.getStartedGameMap().putIfAbsent(gameName, game);
                // Create a new controller for the game and put it in the map
                Controller controller = new Controller(game, this, this.controllerDispatcher);
                this.getGameControllerMap().put(game, controller);
                game.getObserver().rebuildClientList(client);
                return true;
            }
        }
        return false;
    }
}
