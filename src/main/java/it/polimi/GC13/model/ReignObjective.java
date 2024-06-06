package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

public class ReignObjective extends ObjectiveCard {
    public final Resource type; //Which reign is required

    public ReignObjective(int serialNumber, int comboPoints, Resource type) {
        super(serialNumber, comboPoints);
        this.type = type;
    }

    public int getObjectivePoints(Board board) {
        int combo = board.getCollectedResources().get(type)/3; //calculate how many times the obj. has been satisfied
        return combo * getComboPoints();
    }


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
