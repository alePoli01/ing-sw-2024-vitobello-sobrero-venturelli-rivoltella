package it.polimi.GC13.enums;

public enum Position {
    FIRST, SECOND, THIRD, FOURTH;

    public Position next(int playersInGame) {
        if (this == FOURTH) return FIRST;
        if (this == FIRST) return SECOND;
        if (this == SECOND && (playersInGame - 1) == this.ordinal()) return FIRST;
        if (this == THIRD && (playersInGame - 1) == this.ordinal()) return FIRST;
        return values()[this.ordinal() + 1];
    }

    public int getIntPosition() {
        switch (this){
            case FIRST -> {
                return  1;
            }

            case SECOND -> {
                return  2;
            }

            case THIRD -> {
                return  3;
            }

            default -> {
                return 4;
            }
        }

//        if (this == FOURTH) return 4;
//        if (this == FIRST) return 1;
//        if (this == SECOND) return 2;
//        return 3;
    }
}
