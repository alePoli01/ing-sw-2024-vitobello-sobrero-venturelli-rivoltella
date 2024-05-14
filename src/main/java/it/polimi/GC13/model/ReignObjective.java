package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

public class ReignObjective extends ObjectiveCard{
    public final Resource type; //Which reign is required


    //prova
    public ReignObjective(int serialNumber, int comboPoints, Resource type) {
        super(serialNumber, comboPoints);
        this.type = type;
    }


    /*
    public ReignObjective(ReignType type) {
        this.type = type;
        setComboPoints(2);
    }

    public ReignType getObjectiveReign(){
        return this.type;
    }

*/

    public int getObjectivePoints(Board board) {
        int combo = board.getCollectedResources().get(type)/3; //calculate how many times the obj. has been satisfied
        return combo * getComboPoints();
    }

    @Override
    public void printObjectiveCard() {

        //colors of the background
        String backgroundRed = "\u001b[41m";   // red background
        String backgroundGreen = "\u001b[42m"; // Green Background
        String backgroundBlue = "\u001b[46m";  // Blue background
        String backgroundmagenta = "\u001b[45m";  // Blue
        String resetbackground = "\u001b[0m";  // Reset color of the background
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters


        System.out.println("╔═══════╦═╦═══════╗");
        System.out.println("║       ║"+gold+"2"+reset+"║       ║");
        System.out.println("║                 ║");
        System.out.println("║    ["+this.type.toString()+", "+this.type.toString()+", "+this.type.toString()+"]    ║");
        System.out.println("║                 ║");
        System.out.println("╚═════════════════╝");
    }

    public void printLineObjectiveCard(int line) {
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        switch(line){
            case 0:System.out.print("╔═══════╦═╦═══════╗");break;
            case 1:System.out.print("║       ║"+gold+"2"+reset+"║       ║");
            case 2:System.out.print("║                 ║"); break;
            case 3:System.out.print("║    ["+this.type.toString()+", "+this.type.toString()+", "+this.type.toString()+"]    ║"); break;
            case 4:System.out.print("║                 ║"); break;
            case 5:System.out.print("╚═════════════════╝"); break;
        }
    }
    //constructor




}
