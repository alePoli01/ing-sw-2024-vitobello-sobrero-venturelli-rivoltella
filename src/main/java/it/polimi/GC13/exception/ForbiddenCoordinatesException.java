package it.polimi.GC13.exception;

public class ForbiddenCoordinatesException extends Exception{

    public ForbiddenCoordinatesException(int x, int y) {
        super("("+x+"," +y+ ") are not valid coordinates");
    }
}
