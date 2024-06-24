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
    }

    public static Position getPositionFromInt(int integer) {
        switch (integer){
            case 1 -> {
                return  FIRST;
            }

            case 2 -> {
                return SECOND;
            }

            case 3 -> {
                return THIRD;
            }

            default -> {
                return FOURTH;
            }
        }
    }
}
