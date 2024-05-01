package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.LostConnectionToClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.PokeMessage;

import java.io.Serializable;
import java.util.*;

public class LobbyController implements LobbyControllerInterface, LostConnectionToClientInterface {
    private final Map<String, Game> joinableGameMap;
    private final Map<Game, Controller> gameControllerMap; //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;
    private final List<ClientInterface> disconnectedClients = new ArrayList<>();//list of all the disconnected clients

    public LobbyController() {
        this.joinableGameMap = new HashMap<>();
        this.gameControllerMap = new HashMap<>();
    }

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public void checkForExistingGame(ClientInterface client) {
        // upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        Map<String, Integer> gameNameWaitingPlayersMap = new HashMap<>();
        this.joinableGameMap.forEach((gameName, game) -> gameNameWaitingPlayersMap.put(gameName, game.getCurrNumPlayer()));
        client.sendMessage(new OnCheckForExistingGameMessage(gameNameWaitingPlayersMap));
    }

    // create a new game if there is no one available, else it creates a new one
    public synchronized void addPlayerToGame(ClientInterface client, Player player, int playersNumber, String gameName) {
        Game workingGame;
        System.out.println("--Received: PlayerJoiningMessage: [player:" + player.getNickname()+"]");
        // if there is no existing game, a new one is created.
        // create Controller List and Players <-> Game Map
        if (playersNumber >= 2 && playersNumber <= 4) {
            workingGame = new Game(playersNumber, gameName);
            // updates useful maps
            this.gameControllerMap.put(workingGame, new Controller(workingGame, this, this.controllerDispatcher));
            this.joinableGameMap.put(gameName, workingGame);
        } else {
            // adds the player to the model and updates noExistingGame for the next player that wants to play
            workingGame = joinableGameMap.get(gameName);
        }
        // updates controller playerClientMap
        this.gameControllerMap.get(workingGame).getPlayerClientMap().put(player, client);
        // updates controller clientPlayerMap
        this.gameControllerMap.get(workingGame).getClientPlayerMap().put(client, player);
        // set the game for the player
        player.setGame(workingGame);
        // add the player to the model
        this.gameControllerMap.get(workingGame).addPlayerToExistingGame(player, workingGame, client);
        // updates controller dispatcher client <-> player Map
        this.controllerDispatcher.getClientPlayerMap().put(client, player);
        // updates Controller Dispatcher's ClientGameMap adding <client, gamePhase>
        this.controllerDispatcher.getClientControllerMap().put(client, this.gameControllerMap.get(workingGame));
    }

    @Override
    public void connectionLost(ClientInterface client) {
        //when connection is lost put client in waitingToReconnectList
        /*
        cit.
        if the player was assigned to a game that is still running the player state is set to"{PlayerState disconnected}"
         and the client is added to a "{disconnectedButInGame disconnected player list}" until he connects again.
        In addition, if the player was in the "{waitingUsers waiting list}" to play in a match is removed from this one
         */
        System.out.println("la connessione non funziona, metto il client come disconnected");
        disconnectedClients.add(client);
        System.out.println("da qua bisogna capire come ricollegare il client");
    }
}
