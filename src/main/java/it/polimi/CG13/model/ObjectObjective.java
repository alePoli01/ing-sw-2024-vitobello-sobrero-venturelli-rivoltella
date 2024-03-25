package it.polimi.CG13.model;

import it.polimi.CG13.enums.ObjectType;

import java.util.EnumMap;


public class ObjectObjective extends ObjectiveCard{
    private boolean allItems;
    private ObjectType object;

    @Override
    public int getObjectivePoints(Board board) {
        int combo;
        if (allItems) {
            combo = board.getObjectsCollected().get(object);//initialize min value
            for (ObjectType objectType: ObjectType.values()){//check to find true min value
                if (combo>board.getObjectsCollected().get(objectType)){
                    combo=board.getObjectsCollected().get(objectType);//update true min
                }
            }
        } else {
            combo = board.getObjectsCollected().get(object) / 2;
        }
        return combo*getComboPoints();
    }

    //constructors
    public ObjectObjective(ObjectType object) {
        setComboPoints(2);
        this.object=object;
        this.allItems= false;
    }
    public ObjectObjective(boolean allItems) {
        setComboPoints(3);
        this.object = ObjectType.INKWELL;
        this.allItems = allItems;
    }



}
