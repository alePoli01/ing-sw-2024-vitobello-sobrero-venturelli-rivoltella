package it.polimi.GC13.enums;

public enum ReignType implements Resource{
    ANIMAL, FUNGI, INSECT, PLANT;

    @Override
    public boolean equalsAny() {
        return false;
    }
}
