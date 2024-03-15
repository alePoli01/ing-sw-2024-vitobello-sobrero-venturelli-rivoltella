package it.polimi.CG13.model;

import it.polimi.CG13.enums.ReignType;

public class ResourceObjective extends ObjectiveCard{
    private ReignType type; //Which reign is required

    public ResourceObjective(ReignType type) {
        this.type = type;
    }

    @Override
    public void comboCondition() {
        //corpo del metodo
    }
}
