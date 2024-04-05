package it.polimi.GC13.model;

import it.polimi.GC13.enums.EdgeResources;

import java.util.ArrayList;
import java.util.Arrays;


public class ObjectObjective extends ObjectiveCard {
    public final ArrayList<EdgeResources> object;

    //prova
    public ObjectObjective(int serialNumber, int comboPoints, ArrayList<EdgeResources> object) {
        super(serialNumber, comboPoints);
        this.object = new ArrayList<>();//initialize and copy the objects into the card
        this.object.addAll(object);
    }

    public int getObjectivePoints(Board board) {
        int combo;
        if (this.object.containsAll(Arrays.asList(EdgeResources.values()))) {
            combo = board.getCollectedResources().get(object.getFirst());//initialize min value
            for (EdgeResources objectType : EdgeResources.values()) {//check to find true min value
                if (objectType.isObject()) {
                    if (combo > board.getCollectedResources().get(object.getFirst())) {
                        combo = board.getCollectedResources().get(objectType);//update true min
                    }
                }
            }
        } else {
            combo = (board.getCollectedResources().get(object.getFirst())) / 2;
        }
        return combo * getComboPoints();
    }

}
