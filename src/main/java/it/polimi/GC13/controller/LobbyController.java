package it.polimi.GC13.controller;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

public class LobbyController {

    private final Map<Player, Game> playersGameMap;
    private boolean noExistingGame; // true if there is not an existingGame that needs players, false if not
    private Game existingGame;

    public LobbyController() {
        this.noExistingGame = true;
        this.playersGameMap = new HashMap<>();
    }

    public Map<Player, Game> getPlayersGameMap() {
        return playersGameMap;
    }

    // create a new game if there is no one available, else it creates a new one
    public void addPlayerToGame(Player player) throws IOException {
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
            this.getPlayersGameMap().put(player, newGame);
            player.setGame(newGame);
            noExistingGame = false;
            existingGame = newGame;
        } else {
            // if there is no existing game, a new one is created
            this.getPlayersGameMap().put(player, this.existingGame);
            player.setGame(existingGame);
            if (existingGame.numPlayer == existingGame.getCurrNumPlayer()) {
                noExistingGame = false;
            }
        }
    }
}
