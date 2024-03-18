package it.polimi.CG13.model;

import it.polimi.CG13.exception.ForbiddenCoordinates;

public class Coordinates {
    private int x,y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }

    public void evenVerifier() throws ForbiddenCoordinates {
        if (!((x + y) % 2 == 0)) {
            throw new ForbiddenCoordinates(this.x, this.y); // If coordinates are not even, throw expception
        }
    }
}
