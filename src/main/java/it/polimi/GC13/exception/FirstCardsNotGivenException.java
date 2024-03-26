package it.polimi.GC13.exception;

import it.polimi.GC13.model.Player;

public class FirstCardsNotGivenException extends Exception {
    public FirstCardsNotGivenException(Player player) {
            super(player + "didn't receive first cards");
    }
}
