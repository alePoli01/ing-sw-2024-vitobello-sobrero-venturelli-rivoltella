package it.polimi.GC13.model;

import it.polimi.GC13.enums.ObjectType;

import java.util.ArrayList;
import java.util.Arrays;


public class ObjectObjective extends ObjectiveCard{
    public final ArrayList<ObjectType> object;

//prova
    public ObjectObjective(int serialNumber, int comboPoints, ArrayList<ObjectType> object) {
        super(serialNumber, comboPoints);
        this.object = new ArrayList<>();//initialize and copy the objects into the card
        this.object.addAll(object);
    }

    public int getObjectivePoints(Board board) {
        int combo;
        if (this.object.containsAll(Arrays.asList(ObjectType.values()))) {
            combo = board.getObjectsCollected().get(object.getFirst());//initialize min value
            for (ObjectType objectType : ObjectType.values()) {//check to find true min value
                if (combo > board.getObjectsCollected().get(object.getFirst())) {
                    combo = board.getObjectsCollected().get(objectType);//update true min
                }
            }
        } else {
            combo = (board.getObjectsCollected().get(object.getFirst())) / 2;
        }
        return combo * getComboPoints();
    }

}
