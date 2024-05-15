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
        return (this.equals(INKWELL) || this.equals(MANUSCRIPT) || this.equals(QUILL));
    }

    public String toString(){
        String red = "\033[38;2;233;73;23m";   // Red
        String green = "\033[38;2;113;192;124m"; // green
        String blue = "\033[38;2;107;189;192m";  // Blue
        String magenta = "\033[38;2;171;63;148m";  // Magenta
        String gold = "\033[38;2;255;215;0m";  // gold
        String reset = "\u001b[0m";
        /*
            String black = "\u001b[90m";  // black
            String white = "\u001b[37m";
            String mushroom = "\uD83C\uDF44";  // 🍄
            String dog = "\uD83D\uDC36";  // 🐶
            String insect = "\uD83D\uDC1C";  // 🐜
            String plant = "\uD83C\uDF31";  // 🌱
            String manuscript = "\uD83D\uDCDC";  // 📜
            String feather = "\uD83D\uF99C";  // 🪶
            String inkwell = "\uF991";  // 🫙
            String backgroundRed = "\u001b[41m";   // red background
            String backgroundGreen = "\u001b[42m"; // Green Background
            String backgroundBlue = "\u001b[46m";  // Blue background
            String backgroundmagenta = "\u001b[35m";  // Blue
            String resetbackground = "\u001b[0m";  // Reset color of the background
         */

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
                return gold+"F"+reset;
            }
            case MANUSCRIPT -> {
                return gold+"M"+reset;
            }

        }
        return null;
    }
}

