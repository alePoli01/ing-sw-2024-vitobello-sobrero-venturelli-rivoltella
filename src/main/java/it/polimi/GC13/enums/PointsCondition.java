package it.polimi.GC13.enums;

public enum PointsCondition {
    INKWELL, MANUSCRIPT, QUILL, EDGE, NULL;
    String red = "\u001b[31m";   // Red
    String green = "\u001b[32m"; // green
    String blue = "\u001b[36m";  // Blue
    String magenta = "\u001b[35m";  // Magenta
    String gold = "\u001b[93m";  // gold
    String black = "\u001b[90m";  // black
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

    public String toString(){
        switch (this){
            case EDGE -> {
                return gold+"E"+reset;
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
            case NULL->{
                return " ";
            }

        }
        return null;
    }
}

