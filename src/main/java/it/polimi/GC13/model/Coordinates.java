package it.polimi.GC13.model;

import java.io.Serializable;


/**
 * {@code Coordinates} class represents a set of coordinates in a 2-dimensional space where the card has been placed.
 */
public class Coordinates implements Serializable {
    private int x, y;

    /**
     * Constructs a set of coordinates with the specified x and y values.
     *
     * @param x The x-coordinate.
     * @param y The y-coordinate.
     */
    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }
    /**
     * Retrieves the x-coordinate.
     *
     * @return The x-coordinate of the coordinates.
     */
    public int getX() {
        return x;
    }

    /**
     * Sets the x-coordinate to the specified value.
     *
     * @param x The new value for the x-coordinate.
     */
    public void setX(int x) {
        this.x = x;
    }



    /**
     * Retrieves the y-coordinate.
     *
     * @return The y-coordinate of the coordinates.
     */
    public int getY() {
        return y;
    }

    /**
     * Sets the y-coordinate to the specified value.
     *
     * @param y The new value for the y-coordinate.
     */
    public void setY(int y) {
        this.y = y;
    }


    /**
     * Checks if the provided coordinates are equal to this coordinates object.
     *
     * @param coordinatesToCheck The coordinates to compare.
     * @return True if the coordinates have the same x and y values, false otherwise.
     */
    public boolean equals(Coordinates coordinatesToCheck) {
        return this.x == coordinatesToCheck.getX() && this.y == coordinatesToCheck.getY();
    }
}
