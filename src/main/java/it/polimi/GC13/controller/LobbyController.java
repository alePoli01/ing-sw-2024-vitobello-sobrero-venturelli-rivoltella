package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ControllerDispatcher;

import java.io.IOException;
import java.util.*;

public class LobbyController implements  LobbyControllerInterface {
    private final Map<ClientInterface, Controller> clientGamePhaseMap;//links a client to his gamephase
    private int waitingPlayers; // true if there is not an existingGame that needs players, false if not
    private Game existingGame;
    private final ArrayList<Controller> gameController; //contains all ongoing games controller
    private ControllerDispatcher controllerDispatcher;
    // create Controller List and Players <-> Game Map

    public LobbyController() {
        this.waitingPlayers = 0;
        this.clientGamePhaseMap = new HashMap<>();
        this.gameController = new ArrayList<>();
    }

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    public void checkForExistingGame(ClientInterface client) {
        //upon check just provide the answer
        client.onCheckForExistingGame(this.waitingPlayers);
    }

    // create a new game if there is no one available, else it creates a new one
    public void addPlayerToGame(ClientInterface client, Player player, int playersNumber) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {
        // if there is no existing game, a new one is created
        if (playersNumber >= 2 && playersNumber <= 4) {
            Game newGame = new Game(player, playersNumber);
            // adds the controller to the list
            this.gameController.addFirst(new Controller(newGame, this));
            this.waitingPlayers++;
            existingGame = newGame;
        } else {
            // add the player to the existing game and updates noExistingGame for the next player that wants to play
            waitingPlayers = gameController.getFirst().getGameController().addPlayerToExistingGame(player, existingGame);
            System.out.println("waiting players:" + waitingPlayers);
        }
        // update map with the client and the correct controller
        this.clientGamePhaseMap.put(client, this.gameController.getFirst());
        // updates ClientGameMap adding <client, gamePhase>
        controllerDispatcher.getClientGameMap().put(client, this.gameController.getFirst());
        this.playerAddedToGame();
    }

    private void playerAddedToGame() {
        for (ClientInterface client : this.clientGamePhaseMap.keySet()) {
            client.onPlayerAddedToGame(waitingPlayers);
        }
    }
}
