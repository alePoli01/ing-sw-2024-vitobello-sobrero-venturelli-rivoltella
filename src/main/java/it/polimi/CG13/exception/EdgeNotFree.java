package it.polimi.CG13.exception;

import it.polimi.CG13.model.Coordinates;
import it.polimi.CG13.model.PlayableCard;

public class EdgeNotFree extends Exception {

    public EdgeNotFree(Coordinates coordinateToCheck) {
        super(coordinateToCheck + "is not valid");
    }
}
