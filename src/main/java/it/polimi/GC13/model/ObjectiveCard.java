package it.polimi.GC13.model;

public abstract class ObjectiveCard {
    public final int serialNumber;
    public final int comboPoints; //points given by the card

    //prova
    public ObjectiveCard(int serialNumber, int comboPoints) {
        this.serialNumber = serialNumber;
        this.comboPoints = comboPoints;
    }

    public int getSerialNumber() {
        return this.serialNumber;
    }

    public int getComboPoints(){
        return this.comboPoints;
    }

    public abstract int getObjectivePoints(Board board);
}
