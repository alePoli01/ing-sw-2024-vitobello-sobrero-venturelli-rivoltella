package it.polimi.CG13.exception;

public class ForbiddenCoordinatesException extends Exception{

    public ForbiddenCoordinatesException(int x, int y) {
        super(x + y + "are not valid coordinates");
    }
}
