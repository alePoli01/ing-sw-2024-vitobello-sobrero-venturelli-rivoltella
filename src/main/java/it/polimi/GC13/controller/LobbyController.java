package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.LostConnectionToClientInterface;

import java.io.IOException;
import java.util.*;

public class LobbyController implements  LobbyControllerInterface, LostConnectionToClientInterface {
    private final Map<ClientInterface, Controller> clientGamePhaseMap;//links a client to his gamephase
    private final Map<Game, Integer> waitingPlayersMap; // 0 if there isn't an existingGame that needs players, 0 if there isn't a game to join
    private final Map<String, Game> joinableGameMap;
    private final Map<Game, Controller> gameControllerMap; //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;
    // create Controller List and Players <-> Game Map
    private Game workingGame;
    private List<ClientInterface> disconnectedClients = new ArrayList<>();//list of all the disconnected clients

    public LobbyController() {
        this.joinableGameMap = new HashMap<>();
        this.waitingPlayersMap = new HashMap<>();
        this.clientGamePhaseMap = new HashMap<>();
        this.gameControllerMap = new HashMap<>();
    }

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public void checkForExistingGame(ClientInterface client) {
        //upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        client.onCheckForExistingGame(this.joinableGameMap, this.waitingPlayersMap);

    }

    // create a new game if there is no one available, else it creates a new one
    public void addPlayerToGame(ClientInterface client, Player player, int playersNumber, String gameName) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + player.getNickname()+"]");
        // if there is no existing game, a new one is created
        if (playersNumber >= 2 && playersNumber <= 4) {
            Game newGame = new Game(player, playersNumber);
            // updates useful maps
            this.gameControllerMap.put(newGame, new Controller(newGame, this));
            this.waitingPlayersMap.put(newGame, 1);
            this.joinableGameMap.put(gameName, newGame);
            this.workingGame = newGame;
        } else {
            // adds the player to the model and updates noExistingGame for the next player that wants to play
            workingGame = joinableGameMap.get(gameName);
            // updates waiting players in waitingPlayersMap and the model
            this.waitingPlayersMap.put(workingGame, this.gameControllerMap.get(workingGame).addPlayerToExistingGame(player, joinableGameMap.get(gameName)));
            // removes the game from the selectable when all players needed have joined
            if (this.waitingPlayersMap.get(workingGame) == 0) {
                this.joinableGameMap.remove(gameName, workingGame);
            }
            //System.out.println("waiting players:" + waitingPlayersMap);
        }
        player.setGame(workingGame);
        // updates controller dispatcher client <-> player Map
        this.controllerDispatcher.getClientPlayerMap().put(client, player);
        // update map with the client and the correct controller
        this.clientGamePhaseMap.put(client, this.gameControllerMap.get(workingGame));
        // updates Controller Dispatcher's ClientGameMap adding <client, gamePhase>
        this.controllerDispatcher.getClientControllerMap().put(client, this.gameControllerMap.get(workingGame));
        // notify other players when someone connects and how many players are still needed
        this.playerAddedToGame();
    }

    private void playerAddedToGame() {
        for (ClientInterface client : this.clientGamePhaseMap.keySet()) {
            client.onPlayerAddedToGame(this.waitingPlayersMap.get(workingGame));
        }
    }

    @Override
    public void connectionLost(ClientInterface client) {
        //when connection is lost put client in waitingToReconnectList
        /*
        cit.
        if the player was assigned to a game that is still running the player state is set to"{PlayerState disconnected}"
         and the client is added to a "{disconnectedButInGame disconnected player list}" until he connects again.
        In addition if the player was in the "{waitingUsers waiting list}" to play in a match is removed from this one
         */
        System.out.println("la connessione non funziona, metto il client come disconnected");
        disconnectedClients.add(client);
        System.out.println("da qua bisogna capire come ricollegare il client");
    }
}
