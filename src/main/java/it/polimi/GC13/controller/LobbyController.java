package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ControllerDispatcher;
import it.polimi.GC13.network.socket.ServerDispatcher;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LobbyController implements  LobbyControllerInterface {
    private final Map<ClientInterface, Controller> clientGamePhaseMap;//links a client to his gamephase
    private boolean noExistingGame; // true if there is not an existingGame that needs players, false if not
    private Game existingGame;
    private final ArrayList<Controller> gameController;//contains all ongoing games
    private ControllerDispatcher controllerDispatcher;
    // create Controller List and Players <-> Game Map

    public LobbyController() {
        this.noExistingGame = true;
        this.clientGamePhaseMap = new HashMap<>();
        this.gameController = new ArrayList<>();
    }

    public void setControllerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    // create a new game if there is no one available, else it creates a new one
    public void addPlayerToGame(ClientInterface client, Player player) throws IOException, PlayerNotAddedException {
        // if there is no existing game, a new one is created
        if (noExistingGame) {
            System.out.println("There is no game in waiting list. You are creating a new game, how many players will play? [2-4]");
            int playersNumber = 0;
            do {//read the number of players that will play with the client
                BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
                try {
                    playersNumber = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value!");
                }
            } while (playersNumber > 4 || playersNumber < 1);
            // once playersNumber is valid [2-4], then the game is created and adds player to the model
            Game newGame = new Game(player, playersNumber);
            // adds the controller to the list
            this.gameController.addFirst(new Controller(newGame, this));
            // updates model
            newGame.addPlayerToGame(player);
            noExistingGame = false;
            existingGame = newGame;
        } else {
            // add the player to the existing game and updates noExistingGame for the next player that wants to play
            noExistingGame = gameController.getFirst().getGameController().addPlayerToExistingGame(player, existingGame);
        }
        // update map with the client and the correct controller
        this.clientGamePhaseMap.put(client, this.gameController.getFirst());
        // updates client, gamePhase
        controllerDispatcher.getClientGameMap().put(client, this.gameController.getFirst());
    }
}
