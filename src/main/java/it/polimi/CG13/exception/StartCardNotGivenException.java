package it.polimi.CG13.exception;

import it.polimi.CG13.model.Player;

public class StartCardNotGivenException extends Exception {
    public StartCardNotGivenException(Player player) {
        super(player + "didn't receive start card");
    }
}
