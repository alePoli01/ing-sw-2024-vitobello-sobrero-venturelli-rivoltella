package it.polimi.CG13.model;

import it.polimi.CG13.enums.ObjectType;

public class ObjectObjective extends ObjectiveCard{
    private boolean allItems;
    private ObjectType object;

    public ObjectObjective(boolean allItems) {
        this.allItems = allItems;
    }

    @Override
    public void comboCondition() {
         //corpo del metodo
    }
}
