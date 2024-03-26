package it.polimi.GC13.exception;

import it.polimi.GC13.model.Coordinates;

public class EdgeNotFreeException extends Exception {

    public EdgeNotFreeException(Coordinates coordinateToCheck) {
        super(coordinateToCheck + "is not valid");
    }
}
