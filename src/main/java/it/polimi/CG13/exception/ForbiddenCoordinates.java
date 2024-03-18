package it.polimi.CG13.exception;

import it.polimi.CG13.model.Coordinates;

public class ForbiddenCoordinates extends Exception{

    public ForbiddenCoordinates(int x, int y) {
        super(x + y + "are not valid coordinates");
    }
}
