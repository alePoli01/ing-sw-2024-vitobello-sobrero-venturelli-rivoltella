package it.polimi.GC13.enums;

public enum Resource {

    NULL, EMPTY, ANIMAL, FUNGI, INSECT, PLANT, INKWELL, MANUSCRIPT, QUILL;

    public boolean isReign() {
        return (this.equals(ANIMAL) || this.equals(FUNGI) || this.equals(INSECT) || this.equals(PLANT));
    }

    public boolean isObject() {
        return (this.equals(INKWELL) || this.equals(MANUSCRIPT) || this.equals(QUILL));
    }

    public String toString(){
        String red = "\033[38;2;233;73;23m";   // Red
        String green = "\033[38;2;113;192;124m"; // green
        String blue = "\033[38;2;107;189;192m";  // Blue
        String magenta = "\033[38;2;171;63;148m";  // Magenta
        String gold = "\033[38;2;255;215;0m";  // gold
        String reset = "\u001b[0m";

        switch (this){
            case FUNGI -> {
                return red+"F"+reset;
            }
            case ANIMAL -> {
                return blue+"A"+reset;
            }
            case INSECT -> {
                return magenta+"I"+reset;
            }
            case PLANT -> {
                return green+"P"+reset;
            }
            case NULL -> {
                return reset+"X";
            }
            case EMPTY -> {
                return " ";
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
        }
        return null;
    }
}

