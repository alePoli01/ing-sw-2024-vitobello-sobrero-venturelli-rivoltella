package it.polimi.CG13.exception;

import it.polimi.CG13.enums.ReignType;

public class NoResourceAvailable extends Exception {
    public NoResourceAvailable(ReignType reign) {
        super("You don't have enough" + reign + "to play this card");
    }
}
