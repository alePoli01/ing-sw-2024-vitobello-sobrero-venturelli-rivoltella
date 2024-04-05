package it.polimi.GC13.exception;

import it.polimi.GC13.enums.Resource;

public class NoResourceAvailableException extends Exception {
    public NoResourceAvailableException(Resource reign) {
        super("You don't have enough" + reign + "to play this card");
    }
}
