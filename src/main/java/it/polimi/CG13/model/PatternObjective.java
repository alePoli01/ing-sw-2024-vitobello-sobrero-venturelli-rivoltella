package it.polimi.CG13.model;

import it.polimi.CG13.enums.TypePattern;

public class PatternObjective extends ObjectiveCard{
    private TypePattern type;  //what kind of disposition is required
    private int orientation; //orientation of the disposition

    @Override
    public int getObjectivePoints() {
        //corpo del metodo
        return getComboPoints();
    }

    public PatternObjective(TypePattern type) {
        this.type = type;
    }




}
