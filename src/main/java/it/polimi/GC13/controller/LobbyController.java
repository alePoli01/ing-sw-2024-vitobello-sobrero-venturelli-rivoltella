package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class LobbyController {
    private final Map<Player, Game> playersGameMap;
    private boolean noExistingGame; // true if there is not an existingGame that needs players, false if not
    private Game existingGame;
    private final ArrayList<GamePhase> gameController;

    // create Controller List and Players <-> Game Map
    public LobbyController() {
        this.noExistingGame = true;
        this.playersGameMap = new HashMap<>();
        this.gameController = new ArrayList<>();
    }

    public Map<Player, Game> getPlayersGameMap() {
        return playersGameMap;
    }

    // create a new game if there is no one available, else it creates a new one
    public void addPlayerToGame(Player player) throws IOException, PlayerNotAddedException {
        // if there is no existing game, a new one is created
        if (noExistingGame) {
            System.out.println("There is no game in waiting list. You are creating a new game, how many players will play? [2-4]");
            int playersNumber = 0;
            do {
                BufferedReader reader = new BufferedReader(new InputStreamReader((System.in)));
                try {
                    playersNumber = Integer.parseInt(reader.readLine());
                } catch (NumberFormatException e) {
                    System.err.println("Invalid value!");
                }
            } while (playersNumber > 4 || playersNumber < 1);
            // if playersNumber is valid [2-4], then the game is created
            Game newGame = new Game(player, playersNumber);
            // adds the controller to the list
            this.gameController.addFirst(new Controller(newGame));
            // add the player and game to the controllers map
            this.getPlayersGameMap().put(player, newGame);
            // updates model
            newGame.addPlayerToGame(player);
            noExistingGame = false;
            existingGame = newGame;
        } else {
            // update map and call the correct method from controller
            this.getPlayersGameMap().put(player, this.existingGame);
            // add the player to the existing game and updates noExistingGame for the next player that wants to play
            noExistingGame = gameController.getFirst().addPlayerToExistingGame(player, existingGame);
        }
    }
}
