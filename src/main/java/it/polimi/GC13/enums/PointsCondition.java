package it.polimi.GC13.enums;

public enum PointsCondition {
    INKWELL, MANUSCRIPT, QUILL, EDGE, NULL;
    final String gold = "\u001b[93m";  // gold
    final String reset = "\u001b[0m";

    public String toString(){
        switch (this){
            case EDGE -> {
                return gold+"E"+reset;
            }
            case INKWELL -> {
                return gold+"I"+reset;
            }
            case QUILL -> {
                return gold+"Q"+reset;
            }
            case MANUSCRIPT -> {
                return gold+"M"+reset;
            }
            case NULL->{
                return " ";
            }

        }
        return null;
    }
}

