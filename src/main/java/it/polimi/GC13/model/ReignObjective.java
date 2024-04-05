package it.polimi.GC13.model;

import it.polimi.GC13.enums.ReignType;

public class ReignObjective extends ObjectiveCard{
    public final ReignType type; //Which reign is required


    //prova
    public ReignObjective(int serialNumber, int comboPoints, ReignType type) {
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

    @Override
    public int getObjectivePoints(Board board) {
        int combo = board.getCollectedResources().get(type)/3; //calculate how many times the obj. has been satisfied
        return combo * getComboPoints();
    }
    //constructor




}
