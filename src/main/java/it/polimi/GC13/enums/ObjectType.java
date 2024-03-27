package it.polimi.GC13.enums;

public enum ObjectType implements Resource {
    INKWELL, MANUSCRIPT, QUILL;

    @Override
    public boolean equalsAny() {
        return false;
    }
}
