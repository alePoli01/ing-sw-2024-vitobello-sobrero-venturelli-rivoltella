package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.LostConnectionToClientInterface;

import java.io.IOException;
import java.util.*;

public class LobbyController implements  LobbyControllerInterface, LostConnectionToClientInterface {
    private final Map<Game, Integer> connectedPlayersMap; // 0 if there isn't an existingGame that needs players, 0 if there isn't a game to join
    private final Map<String, Game> joinableGameMap;
    private final Map<Game, Controller> gameControllerMap; //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;
    // create Controller List and Players <-> Game Map
    private Game workingGame;
    private List<ClientInterface> disconnectedClients = new ArrayList<>();//list of all the disconnected clients

    public LobbyController() {
        this.joinableGameMap = new HashMap<>();
        this.connectedPlayersMap = new HashMap<>();
        this.gameControllerMap = new HashMap<>();
    }

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public void checkForExistingGame(ClientInterface client) {
        //upon check just provide the answer
        System.out.println("--Received: checkForExistingGame");
        client.onCheckForExistingGame(this.joinableGameMap, this.connectedPlayersMap);
    }

    // create a new game if there is no one available, else it creates a new one
    public synchronized void addPlayerToGame(ClientInterface client, Player player, int playersNumber, String gameName) throws IOException, PlayerNotAddedException {
        System.out.println("--Received: PlayerJoiningMessage: [player:" + player.getNickname()+"]");
        // if there is no existing game, a new one is created
        if (playersNumber >= 2 && playersNumber <= 4) {
            workingGame = new Game(playersNumber);
            // updates useful maps
            this.gameControllerMap.put(workingGame, new Controller(workingGame, this, this.controllerDispatcher));
            this.connectedPlayersMap.put(workingGame, 1);
            this.joinableGameMap.put(gameName, workingGame);
        } else {
            // adds the player to the model and updates noExistingGame for the next player that wants to play
            /*if (joinableGameMap.get(gameName).getNumPlayer() == joinableGameMap.get(gameName).getCurrNumPlayer()) {
                //if the game started while the player was joining the request gets rejected (missing parameters)
                System.out.println("\tGame is already full, rejecting add request");
                client.onCheckForExistingGame(this.joinableGameMap, this.connectedPlayersMap);
            }*/
            workingGame = joinableGameMap.get(gameName);
            // updates waitingPlayersMap
            this.connectedPlayersMap.put(workingGame, this.connectedPlayersMap.get(workingGame) + 1);
        }
        // updates controller clientPlayerMap
        this.gameControllerMap.get(workingGame).getClientPlayerMap().put(player, client);
        // set the game for the player
        player.setGame(workingGame);
        try {
            // add the player to the model
            this.gameControllerMap.get(workingGame).addPlayerToExistingGame(player, workingGame);
        } catch (NicknameAlreadyTakenException e) {
            client.inputExceptionHandler(new NicknameAlreadyTakenException(e.getPlayerList(), connectedPlayersMap, joinableGameMap));
        }

        // removes the game from the selectable when all players needed have joined
        /*if (this.connectedPlayersMap.get(workingGame) == workingGame.numPlayer) {
            this.joinableGameMap.remove(gameName, workingGame);
        }*/
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
        In addition if the player was in the "{waitingUsers waiting list}" to play in a match is removed from this one
         */
        System.out.println("la connessione non funziona, metto il client come disconnected");
        disconnectedClients.add(client);
        System.out.println("da qua bisogna capire come ricollegare il client");
    }
}
