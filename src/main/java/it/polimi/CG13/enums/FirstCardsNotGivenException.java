package it.polimi.CG13.enums;

import it.polimi.CG13.model.Player;

public class FirstCardsNotGivenException extends Exception {
    public FirstCardsNotGivenException(Player player) {
            super(player + "didn't receive first cards");
    }
}
