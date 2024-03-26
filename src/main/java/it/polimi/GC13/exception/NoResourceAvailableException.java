package it.polimi.GC13.exception;

import it.polimi.GC13.enums.ReignType;

public class NoResourceAvailableException extends Exception {
    public NoResourceAvailableException(ReignType reign) {
        super("You don't have enough" + reign + "to play this card");
    }
}
