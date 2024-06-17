package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;

import java.util.ArrayList;


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
        ArrayList<Resource> allItems= new ArrayList<>();
        allItems.add(Resource.INKWELL);
        allItems.add(Resource.MANUSCRIPT);
        allItems.add(Resource.QUILL);
        if (this.object.containsAll(allItems) ) {
            combo = board.getCollectedResources().get(object.getFirst());//initialize min value
            for (Resource objectType : Resource.values()) {//check to find true min value
                if (objectType.isObject()) {
                    if (combo > board.getCollectedResources().get(objectType)) {
                        combo = board.getCollectedResources().get(objectType);//update true min
                    }
                }
            }
        } else {
            combo = (board.getCollectedResources().get(object.getFirst())) / 2;
        }
        return combo * this.comboPoints;
    }

    @Override
    public void printLineObjectiveCard(int line) {
        String gold = "\033[38;2;255;215;0m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        switch (this.object.size()) {
            case 3: {
                switch (line) {
                    case 0: {
                        System.out.print("╔═════════════════╗");
                        break;
                    }
                    case 1: {
                        System.out.print("║       ║" + gold + this.object.size() + reset + "║       ║");
                        break;
                    }
                    case 2, 4: {
                        System.out.print("║                 ║");
                        break;
                    }
                    case 3: {
                        System.out.print("║    " + this.object + "    ║");
                        break;
                    }
                    case 5: {
                        System.out.print("╚═════════════════╝");
                        break;
                    }
                }
                break;
            }
            case (1): {
                switch (line) {
                    case 0: {
                        System.out.print("╔═════════════════╗");
                        break;
                    }
                    case 1: {
                        System.out.print("║       ║" + gold + (this.object.size() + 1) + reset + "║       ║");
                        break;
                    }
                    case 2, 4: {
                        System.out.print("║                 ║");
                        break;
                    }
                    case 3: {
                        System.out.print("║     " + this.object + " " + this.object + "     ║");
                        break;
                    }
                    case 5: {
                        System.out.print("╚═════════════════╝");
                        break;
                    }
                }
                break;
            }
        }
    }
}




