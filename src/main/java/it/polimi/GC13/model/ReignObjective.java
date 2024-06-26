package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

/**
 * Represents a reign objective card that specifies points
 * based on the number of times a specific reign resource has been collected.
 */
public class ReignObjective extends ObjectiveCard {
    public final Resource type; //Which reign is required

    /**
     * Constructs a {@code ReignObjective} instance with the specified parameters.
     *
     * @param serialNumber The serial number of the objective card.
     * @param comboPoints  The points awarded by this objective card.
     * @param type         The type of reign resource required by this objective card.
     */
    public ReignObjective(int serialNumber, int comboPoints, Resource type) {
        super(serialNumber, comboPoints);
        this.type = type;
    }

    /**
     * Calculates the total points achieved for this objective card
     * based on the collected resources of the specified type on the board.
     *
     * @param board The board object representing the current game state.
     * @return The total points achieved for this objective card.
     */
    public int getObjectivePoints(Board board) {
        int combo = board.getCollectedResources().get(type)/3; //calculate how many times the obj. has been satisfied
        return combo * this.comboPoints;
    }

    /**
     * Prints the objective card representation for a specific line.
     *
     * @param line The line number to print the objective card representation.
     */
    public void printLineObjectiveCard(int line) {
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        switch(line){
            case 0:System.out.print("╔═════════════════╗");break;
            case 1:System.out.print("║       ║"+gold+"2"+reset+"║       ║");break;
            case 2, 4:System.out.print("║                 ║"); break;
            case 3:System.out.print("║    ["+this.type+", "+this.type+", "+this.type+"]    ║"); break;
            case 5:System.out.print("╚═════════════════╝"); break;
        }
    }
}
