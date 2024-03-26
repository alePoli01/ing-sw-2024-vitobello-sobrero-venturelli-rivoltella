package it.polimi.GC13.exception;

import it.polimi.GC13.model.Player;

public class StartCardNotGivenException extends Exception {
    public StartCardNotGivenException(Player player) {
        super(player + "didn't receive start card");
    }
}
