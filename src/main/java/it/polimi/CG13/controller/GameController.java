package it.polimi.CG13.controller;

import it.polimi.CG13.model.*;

public class GameController {
    private Server serverId;


    public void addPlayerToGame(Player player, Game game) {
        if (serverId.getGameList().contains(game)) {
            serverId.getPlayersGameMap().put(player, game);
        }
    }
}
