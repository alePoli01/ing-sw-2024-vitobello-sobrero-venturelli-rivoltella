package it.polimi.GC13.enums;

public enum Resource {

    NULL, EMPTY, ANIMAL, FUNGI, INSECT, PLANT, INKWELL, MANUSCRIPT, QUILL;
    String red = "\u001b[31m";   // Red
    String green = "\u001b[32m"; // green
    String blue = "\u001b[36m";  // Blue
    String magenta = "\u001b[35m";  // Magenta
    String gold = "\u001b[93m";  // gold
    String black = "\u001b[90m";  // black
    String white = "\u001b[37m";
    String reset = "\u001b[0m";
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
    public String toString(int edge){

        String goUP="\u001B[1A";
        String goDOWN="\u001B[1B";
        String goRIGHT="\u001B[1C";
        String goLEFT="\u001B[1D";
        String SAVE="\u001B[s";
        String RESET="\u001B[u";
        String DELETE="\b";
        System.out.println("abcd");
        switch(edge){
            case(0):{
                System.out.print(SAVE+goUP+goRIGHT+"ciao"+RESET+"ciao");
                break;
            }
            case(1):{
                break;
            }
            case(2):{
                break;
            }
            case(3):{
                break;
            }
        }
        return null;
    }
}

