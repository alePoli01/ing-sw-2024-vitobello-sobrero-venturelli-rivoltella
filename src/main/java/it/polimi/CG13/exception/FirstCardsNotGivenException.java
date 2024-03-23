package it.polimi.CG13.exception;

import it.polimi.CG13.model.Player;

public class FirstCardsNotGivenException extends Exception {
    public FirstCardsNotGivenException(Player player) {
            super(player + "didn't receive first cards");
    }
}
