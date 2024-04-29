package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.io.Serializable;
import java.util.Map;

public class PlayableCard implements Serializable {
    public final int serialNumber;
    public final Resource reign;
    public final CardType cardType;
    public final Resource[] edgeResource;
    public final Map<Resource, Integer> resourceNeeded;
    public final int pointsGiven;
    public final PointsCondition condition;
    private PlayableCard[] linkedCard;

    public PlayableCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition) {
        this.serialNumber = serialNumber;
        this.reign = reign;
        this.cardType = cardType;
        this.edgeResource = edgeResource;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
        this.condition = condition;
    }

    // method to calculate points given after a gold card is placed
    public int getPointsGiven(Board board, Coordinates xy) {
        if (this.condition != null) {
            return switch (condition) {
                case QUILL -> board.getCollectedResources().get(Resource.QUILL) * pointsGiven;
                case MANUSCRIPT -> board.getCollectedResources().get(Resource.MANUSCRIPT) * pointsGiven;
                case INKWELL -> board.getCollectedResources().get(Resource.INKWELL) * pointsGiven;
                case EDGE -> board.surroundingCardsNumber(xy) * pointsGiven;
                case NULL -> pointsGiven;
            };
        } else {
            return pointsGiven;
        }
    }
    public void cardPrinter(boolean isflipped){

        //colors of the characters
        String red = "\u001b[31m";   // Red
        String green = "\u001b[32m"; // green
        String blue = "\u001b[36m";  // Blue
        String magenta = "\u001b[35m";  // Magenta
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters

        //colors of the background
        String backgroundRed = "\u001b[41m";   // red background
        String backgroundGreen = "\u001b[42m"; // Green Background
        String backgroundBlue = "\u001b[46m";  // Blue background
        String backgroundmagenta = "\u001b[35m";  // Blue
        String resetbackground = "\u001b[0m";  // Reset color of the background

        /*
        Linea orizzontale: ═ (U+2500)
        Linea verticale: ║ (U+2502)
        Angolo in alto a sinistra: ╔ (U+250C)
        Angolo in alto a destra: ╗ (U+2510)
        Angolo in basso a sinistra: ╚ (U+2514)
        Angolo in basso a destra: ╝ (U+2518)
        Linea a T orizzontale (intersezione): ╦ (U+252C), ╩ (U+2534)
        Linea a T verticale (intersezione): ╠ (U+251C), ╣ (U+2524)
        Croce: ┼ (U+253C)
        */
        if (this.serialNumber <= 40) {
            //resource card

            if (isflipped) {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║" + this.edgeResource[3].toString() + "║    " + gold + this.pointsGiven + reset + "    ║" + this.edgeResource[2].toString() + "║");
                System.out.println("╠═══╝         ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║" + this.edgeResource[0].toString() + "║         ║" + this.edgeResource[1].toString() + "║");
                System.out.println("╚═══╩═════════╩═══╝");
            } else {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║   ║         ║   ║");
                System.out.println("╠═══╝   " + this.reign.toString() + "   ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║   ║         ║   ║");
                System.out.println("╚═══╩═════════╩═══╝");
            }


        }
        if (this.serialNumber > 40 && this.serialNumber <= 80) {
            //gold card


            if (isflipped) {
                System.out.println("╔═══╦════╦════╦═══╗");
                System.out.println("║" + this.edgeResource[3].toString() + "║   " + gold + this.pointsGiven + reset + "║" + gold + this.condition.toString() + reset + "   ║" + this.edgeResource[2].toString() + "║");
                System.out.println("╠═══╝         ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║" + this.edgeResource[0].toString() + "║         ║" + this.edgeResource[1].toString() + "║");
                System.out.println("╚═══╩═════════╩═══╝");
            } else {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║   ║         ║   ║");
                System.out.println("╠═══╝   " + this.reign.toString() + "   ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║   ║         ║   ║");
                System.out.println("╚═══╩═════════╩═══╝");
            }


        }

    }
}
