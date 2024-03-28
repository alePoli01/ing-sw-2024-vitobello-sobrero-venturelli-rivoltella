package it.polimi.GC13.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Server {

    private final Map<Player, Game> playersGameMap;

    public Server() {
        this.playersGameMap = new HashMap<>();
    }

    public Map<Player, Game> getPlayersGameMap() {
        return playersGameMap;
    }

    // create the game when the first player joins and it adds him to the GameMap
    public void createGame(Player player, int playersNumber) {
        Game newGame = new Game(player, playersNumber);
        this.getPlayersGameMap().put(player, newGame);
        player.setGame(newGame);
    }
}
