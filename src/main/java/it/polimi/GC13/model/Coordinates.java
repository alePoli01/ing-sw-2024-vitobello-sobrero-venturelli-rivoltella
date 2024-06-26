package it.polimi.GC13.model;

import java.io.Serializable;

/**
 * Class that represents the coordinate where the card has been placed
 */
public class Coordinates implements Serializable {
    private int x, y;

    /**
     * constructor
     * @param x coordinate x
     * @param y coordinate y
     */
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

    public boolean equals(Coordinates coordinatesToCheck) {
        return this.x == coordinatesToCheck.getX() && this.y == coordinatesToCheck.getY();
    }
}
