package it.polimi.CG13.model;

import it.polimi.CG13.enums.TypePattern;

public class PatternObjective extends ObjectiveCard{
    private TypePattern type;  //what kind of disposition is required
    private int orientation; //orientation of the disposition



    public PatternObjective(TypePattern type) {
        this.type = type;
    }
    @Override
    public void comboCondition() {
        //corpo del metodo
    }



}
