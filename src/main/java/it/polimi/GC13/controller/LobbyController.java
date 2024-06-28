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

/**
 * The {@code LobbyController} class manages the state of games and players in the lobby system.
 * It handles operations such as creating new games, adding players to games, checking for existing games,
 * and reconnecting players to ongoing games.
 */
public class LobbyController implements Serializable {
    /**
     * Map storing joinable games where games are mapped by their unique game name.
     */
    private final Map<String, Game> joinableGameMap = new ConcurrentHashMap<>();

    /**
     * Map storing started games where games are mapped by their unique game name.
     */
    private final Map<String, Game> startedGameMap = new ConcurrentHashMap<>();

    /**
     * Map associating ongoing games with their respective `Controller` instances.
     * This map contains all ongoing games and their controllers.
     */
    private transient final Map<Game, Controller> gameControllerMap = new ConcurrentHashMap<>(); //contains all ongoing games controller

    /**
     * The {@link ControllerDispatcher} responsible for dispatching game-related operations.
     */
    private ControllerDispatcher controllerDispatcher;

    /**
     * Manages disk operations for saving and retrieving game states.
     */
    private final DiskManager diskManager = new DiskManager();

    /**
     * List of game names that are in the process of reconnecting.
     */
    private List<String> gamesReconnecting = new ArrayList<>();

    /**
     * Map associating clients ({@link ClientInterface}) with their respective {@link ServerConnectionTimer} instances.
     * Used for managing client connections and timeouts.
     */
    private final Map<ClientInterface, ServerConnectionTimer> connectionTimerMap = new HashMap<>();



    /**
     * Sets the controller dispatcher for managing client-server interactions.
     *
     * @param controllerDispatcher the dispatcher to handle client requests.
     */
    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    /**
     * Retrieves the map of joinable games.
     *
     * @return the map containing joinable games by name.
     */
    public Map<String, Game> getJoinableGameMap() {
        return this.joinableGameMap;
    }


    /**
     * Retrieves the map of started games.
     *
     * @return the map containing started games by name.
     */
    public Map<String, Game> getStartedGameMap() {
        return this.startedGameMap;
    }


    /**
     * Sends information about existing joinable games to the client.
     *
     * @param client the client interface to send the message to.
     * @throws RemoteException if there's an issue communicating with the client remotely.
     */
    public void checkForExistingGame(ClientInterface client) throws RemoteException {
        //upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        Map<String, Integer> gameNameWaitingPlayersMap = new ConcurrentHashMap<>();
        this.joinableGameMap.forEach((gameName, game) -> gameNameWaitingPlayersMap.put(gameName, game.getCurrNumPlayer()));
        client.sendMessageFromServer(new OnCheckForExistingGameMessage(gameNameWaitingPlayersMap));
    }

    /**
     * Adds a player to an existing game based on the provided player nickname and game name.
     *
     * @param client the client interface representing the player.
     * @param playerNickname the nickname of the player to add.
     * @param gameName the name of the game to add the player to.
     * @throws RemoteException if there's an issue communicating with the client remotely.
     */
    public synchronized void addPlayerToGame(ClientInterface client, String playerNickname, String gameName) throws RemoteException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + playerNickname + "]");
        try {
            // creates the player
            Player player = new Player(playerNickname);
            Game workingGame = joinableGameMap.get(gameName);
            // updates controller playerClientMap
            // updates controller clientPlayerMap
            this.gameControllerMap.get(workingGame).addToMaps(player, client);
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
     * Creates a new game with the specified parameters.
     *
     * @param client the client interface initiating the game creation.
     * @param playerNickname the nickname of the player creating the game.
     * @param playersNumber the number of players for the game.
     * @param gameName the name of the game to create.
     * @throws RemoteException if there's an issue communicating with the client remotely.
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


    /**
     * Reconnects a player to an existing game.
     *
     * @param client the client interface representing the player.
     * @param gameName the name of the game to reconnect to.
     * @param playerName the name of the player to reconnect.
     * @throws RemoteException if there's an issue communicating with the client remotely.
     */
    public synchronized void reconnectPlayerToGame(ClientInterface client, String gameName, String playerName) throws RemoteException {
        System.out.println("--Received: reconnectPlayerToGame : [player:" + playerName +" gameName:"+gameName + "]");

        if (this.restartGames(gameName, playerName, client)) {
            System.out.println("\033[0;35mGame and Player name was found, " + playerName + "'s client has been reconnected\033[0m");
            client.sendMessageFromServer(new OnReconnectPlayerToGameMessage());
        } else {
            System.err.println("[player: " + playerName + "] or [game:  " + gameName + "] were not found in game.");
            client.sendMessageFromServer(new OnPlayerNotReconnectedMessage(playerName));
        }
    }


    /**
     * Restarts a game from disk or existing maps and reconnects the client to it.
     *
     * @param gameName the name of the game to reconnect to.
     * @param playerName the name of the player to reconnect.
     * @param client the client interface representing the player.
     * @return true if the game and player were successfully reconnected; false otherwise.
     */
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
                    startTimer(client);
                    game.getPlayerList().stream().findFirst().ifPresent(player -> {
                        this.gameControllerMap.get(game).addToMaps(player, client);
                        System.out.println("controller maps updated");
                    });
                    return true;
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Starts a timer for the client to monitor connection status.
     *
     * @param client the client interface to start the timer for.
     */
    public void startTimer(ClientInterface client) {
        ServerConnectionTimer connectionTimer = new ServerConnectionTimer(client, this);
        this.connectionTimerMap.put(client, connectionTimer);
    }

    /**
     * Closes the game associated with the client.
     *
     * @param client the client interface representing the player.
     */
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
        //notify and remove client-player entry
        this.gameControllerMap.get(player.getGame()).closeGame(client);

        //cleanup lobbyController's maps
        this.gameControllerMap.remove(player.getGame());
        if(!joinableGameMap.containsKey(gameToClose)) {
            this.startedGameMap.remove(gameToClose);
        }else{
            joinableGameMap.remove(gameToClose);
        }
    }

    /**
     * Handles the response from a client to a ping message (pong message).
     *
     * @param client The `ClientInterface` representing the client responding with a pong message.
     */
    public void pongAnswer(ClientInterface client) {
        //ping is sent by the ServerImpulse no need to answer here
        connectionTimerMap.get(client).stopTimer();
        connectionTimerMap.get(client).startTimer();
    }
}
