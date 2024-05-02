package it.polimi.GC13.enums;

public enum Position {
    FIRST, SECOND, THIRD, FOURTH;

    public Position next(int numPlayer) {
        if (this == FOURTH) return FIRST;
        if (this == FIRST) return SECOND;
        if (this == SECOND && (numPlayer - 1) == this.ordinal()) return FIRST;
        if (this == THIRD && (numPlayer - 1) == this.ordinal()) return FIRST;
        return values()[this.ordinal() + 1];
    }
}
