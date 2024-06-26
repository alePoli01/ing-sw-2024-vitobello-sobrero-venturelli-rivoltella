package it.polimi.GC13.model;

import java.io.Serializable;

/**
 * Abstract class representing an objective card in the game.
 */
public abstract class ObjectiveCard implements Serializable {
    public final int serialNumber;
    public final int comboPoints; //points given by the card

    /**
     * Constructs an objective card with the specified serial number and combo points.
     *
     * @param serialNumber The serial number of the card.
     * @param comboPoints  The points given by the card.
     */
    public ObjectiveCard(int serialNumber, int comboPoints) {
        this.serialNumber = serialNumber;
        this.comboPoints = comboPoints;
    }

    /**
     * Retrieves the objective points awarded by this card based on the state of the board.
     *
     * @param board The game board on which the objective points are calculated.
     * @return The objective points awarded by this card.
     */
    public abstract int getObjectivePoints(Board board);

    /**
     * Prints a line representing the objective card, typically for display purposes.
     *
     * @param line The line number to print.
     */
    public abstract void printLineObjectiveCard(int line);
}
