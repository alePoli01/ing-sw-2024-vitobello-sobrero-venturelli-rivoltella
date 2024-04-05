package it.polimi.GC13.exception;

import it.polimi.GC13.enums.EdgeResources;

public class NoResourceAvailableException extends Exception {
    public NoResourceAvailableException(EdgeResources reign) {
        super("You don't have enough" + reign + "to play this card");
    }
}
