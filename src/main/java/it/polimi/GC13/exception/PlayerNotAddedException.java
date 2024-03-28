package it.polimi.GC13.exception;

import it.polimi.GC13.model.Player;

public class PlayerNotAddedException extends Exception {
    public PlayerNotAddedException(Player player) {
        super(player.getNickname() + " hasn't added to the game.");
    }
}
