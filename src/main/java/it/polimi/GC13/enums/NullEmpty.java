package it.polimi.GC13.enums;

public enum NullEmpty implements Resource {
    NULL, EMPTY;

    // returns true if the edge equals NULL or EMPTY
    @Override
    public boolean equalsAny() {
        return (this.equals(NULL) || this.equals(EMPTY));
    }
}
