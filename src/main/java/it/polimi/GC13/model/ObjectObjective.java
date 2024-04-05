package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

import java.util.ArrayList;
import java.util.Arrays;


public class ObjectObjective extends ObjectiveCard {
    public final ArrayList<Resource> object;

    //prova
    public ObjectObjective(int serialNumber, int comboPoints, ArrayList<Resource> object) {
        super(serialNumber, comboPoints);
        this.object = new ArrayList<>();//initialize and copy the objects into the card
        this.object.addAll(object);
    }

    public int getObjectivePoints(Board board) {
        int combo;
        if (this.object.containsAll(Arrays.asList(Resource.values()))) {
            combo = board.getCollectedResources().get(object.getFirst());//initialize min value
            for (Resource objectType : Resource.values()) {//check to find true min value
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
