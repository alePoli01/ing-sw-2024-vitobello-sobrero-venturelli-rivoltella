package it.polimi.CG13.model;

import it.polimi.CG13.enums.ReignType;

import java.util.EnumMap;

public class ReignObjective extends ObjectiveCard{
    private ReignType type; //Which reign is required

    public int getObjectivePoints(EnumMap<ReignType,Integer> reignsCollected) {
        int combo= reignsCollected.get(type)/3; //calculate how many times the obj. has been satisfied
        return combo*getComboPoints();
    }
    //constructor
    public ReignObjective(ReignType type) {
        this.type = type;
        setComboPoints(2);
    }

    public ReignType getObjectiveReign(){
        return this.type;
    }



}
