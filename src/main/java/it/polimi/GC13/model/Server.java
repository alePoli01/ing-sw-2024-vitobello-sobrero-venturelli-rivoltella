package it.polimi.GC13.model;

import java.util.ArrayList;
import java.util.Map;

public class Server {
    private ArrayList<Game>  gameList;
    private Map<Player, Game> playersGameMap;

    public ArrayList<Game> getGameList() {
        return gameList;
    }

    public Map<Player, Game> getPlayersGameMap() {
        return playersGameMap;
    }

    public void addPlayerToGame(Player player, Game game) {
        if (this.getGameList().contains(game)) {
            this.getPlayersGameMap().put(player, game);
        }
    }
}
