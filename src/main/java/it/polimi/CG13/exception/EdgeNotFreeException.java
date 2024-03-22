package it.polimi.CG13.exception;

import it.polimi.CG13.model.Coordinates;

public class EdgeNotFreeException extends Exception {

    public EdgeNotFreeException(Coordinates coordinateToCheck) {
        super(coordinateToCheck + "is not valid");
    }
}
