package it.polimi.GC13.controller;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerConnectionTimer;
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
    private final DiskManager diskManager = new DiskManager();
    private List<String> gamesReconnecting = new ArrayList<>();
    private final Map<ClientInterface, ServerConnectionTimer> connectionTimerMap = new HashMap<>();

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
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
     * METHOD TO ADD EACH PLAYER TO THE GAME
     */
    public synchronized void addPlayerToGame(ClientInterface client, String playerNickname, String gameName) throws RemoteException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + playerNickname + "]");
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
            startTimer(client);
        } catch (GenericException e) {
            client.sendMessageFromServer(new OnNickNameAlreadyTakenMessage(playerNickname));
            System.err.println(e.getMessage());
        }
    }

    /**
     * METHOD TO CREATE A NEW GAME
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
        System.out.println("--Received: reconnectPlayerToGame : [player:" + playerName +" gameName:"+gameName + "]");

        if (this.restartGames(gameName, playerName, client)) {
            System.out.println("\033[0;35mGame and Player name was found, reconnecting " + playerName + "'s client\033[0m");
            client.sendMessageFromServer(new OnReconnectPlayerToGameMessage());
        } else {
            System.err.println("[player: " + playerName + "] or [game:  " + gameName + "] were not found in game.");
            client.sendMessageFromServer(new OnPlayerNotReconnectedMessage(playerName));
        }
    }

    public boolean restartGames(String gameName, String playerName, ClientInterface client) {
        Game game;
        if (!this.gamesReconnecting.contains(gameName)) {
            //if the game hasn't been re-opened by someone, read it on the disk
            System.out.println("Reading data from disk for game: " + gameName);
            game = diskManager.readFromDisk(gameName);
            gamesReconnecting.add(gameName);
        } else {
            //if the disk has already been read get the game from the started map
            System.out.println("data for game: " + gameName + " already read");
            game = this.startedGameMap.get(gameName);
        }

        try {
            if (game != null) {
                System.out.println("Found serialized file: " + game.getGameName());
                if (game.getPlayerList().stream().anyMatch(player -> player.getNickname().equals(playerName))) {
                    if (!this.getStartedGameMap().containsKey(gameName)) {
                        this.getStartedGameMap().put(gameName, game);
                        //create listeners list and add the player as a listener
                        game.setObserver();
                        game.getObserver().addListener(client);
                        // Create a new controller for the game and put it in the map
                        Controller controller = new Controller(game.getGameState(), game, this, this.controllerDispatcher);
                        //map the found game with the new controller
                        this.gameControllerMap.put(game, controller);
                        System.out.println("added game with controller: " + controller.getGameController().getClass().getSimpleName());
                    } else {
                        this.getStartedGameMap().get(gameName).getObserver().addListener(client);
                    }
                    // updates controller dispatcher client <-> player Map
                    this.controllerDispatcher.getClientPlayerMap().put(client, game.getPlayerList().stream().filter(player -> player.getNickname().equals(playerName)).toList().getFirst());
                    // updates Controller Dispatcher's ClientGameMap adding <client, gamePhase>
                    this.controllerDispatcher.getClientControllerMap().put(client, this.gameControllerMap.get(game));
                    /*
                    TODO: far ripartire i timer di chi si ricollega
                            se ci si scollega quando gli altri non sono ancora collegati è da controllare
                     */
                    startTimer(client);
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    public void startTimer(ClientInterface client) {
        ServerConnectionTimer connectionTimer = new ServerConnectionTimer(client, this);
        this.connectionTimerMap.put(client, connectionTimer);
    }

    public void closeGame(ClientInterface client) {

        Player player = controllerDispatcher.getClientPlayerMap().get(client);
        //block other timers
        Set<ClientInterface> clientsToDisconnect = this.gameControllerMap.get(player.getGame()).getClientPlayerMap().keySet();
        clientsToDisconnect.forEach(clientToDisconnect -> {
            ServerConnectionTimer timer = connectionTimerMap.get(clientToDisconnect);
            if(timer != null) {
                connectionTimerMap.get(clientToDisconnect).invalidateTimer();
                connectionTimerMap.remove(clientToDisconnect);
            }
        });
        //fetch data to notify clients
        String gameToClose = player.getGame().getGameName();
        System.out.println("\033[0;35mClosing Game: " + gameToClose + "\033[0m");
        this.gameControllerMap.get(player.getGame()).closeGame(client);

        //cleanup the maps
        gameControllerMap.remove(player.getGame());
        if(!joinableGameMap.containsKey(gameToClose)) {
            this.startedGameMap.remove(gameToClose);
        }else{
            joinableGameMap.remove(gameToClose);
        }
    }

    public void pongAnswer(ClientInterface client) {
        //ping is sent by the ServerImpulse no need to answer here
        connectionTimerMap.get(client).stopTimer();
        connectionTimerMap.get(client).startTimer();
    }
}
