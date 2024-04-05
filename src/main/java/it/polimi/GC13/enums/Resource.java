package it.polimi.GC13.enums;

public enum Resource {

    NULL, EMPTY, ANIMAL, FUNGI, INSECT, PLANT, INKWELL, MANUSCRIPT, QUILL;

    // returns true if the edge equals NULL or EMPTY
    public boolean isNullOrEmpty() {
        return (this.equals(NULL) || this.equals(EMPTY));
    }

    public boolean isReign() {
        return (this.equals(ANIMAL) || this.equals(FUNGI) || this.equals(INSECT) || this.equals(PLANT));
    }

    public boolean isObject() {
        return (this.equals(ANIMAL) || this.equals(FUNGI) || this.equals(INSECT) || this.equals(PLANT));
    }
}
