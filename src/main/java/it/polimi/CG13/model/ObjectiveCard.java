package it.polimi.CG13.model;

public abstract class ObjectiveCard {
    private int comboPoints; //points given by the card

    public int getComboPoints(){
        return this.comboPoints;
    }
    public void setComboPoints(int comboPoints) {
        this.comboPoints = comboPoints;
    }
    public int getObjectivePoints() {
        return comboPoints;
    }
}
