package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;
import java.util.ArrayList;

public class StartCard extends PlayableCard {
    public final Resource[] frontReigns;
    public final Resource[] reignBackPointEdge;


    public StartCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, Resource[] frontReigns, Resource[] reignBackPointEdge) {
        super(serialNumber, reign, cardType, edgeResource, resourceNeeded, pointsGiven, condition);
        this.frontReigns = frontReigns;
        this.reignBackPointEdge = reignBackPointEdge;
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

        if (!isflipped) {
            System.out.println("╔═══╦═════════╦═══╗");
            System.out.println("║ " + this.edgeResource[3].toString() + " ║         ║ " + this.edgeResource[2].toString() + " ║");
            System.out.println("╠═══╝  │"+ this.frontReigns[0].toString() +this.frontReigns[1].toString() +this.frontReigns[2].toString() +"│  ╚═══╣");
            System.out.println("╠═══╗         ╔═══╣");
            System.out.println("║ " + this.edgeResource[0].toString() + " ║         ║ " + this.edgeResource[1].toString() + " ║");
            System.out.println("╚═══╩═════════╩═══╝");
        } else {
            System.out.println("╔═══╦═════════╦═══╗");
            System.out.println("║ " + this.reignBackPointEdge[3].toString() + " ║         ║ " + this.reignBackPointEdge[2].toString() + " ║");
            System.out.println("╠═══╝         ╚═══╣");
            System.out.println("╠═══╗         ╔═══╣");
            System.out.println("║ " + this.reignBackPointEdge[0].toString() + " ║         ║ " + this.reignBackPointEdge[1].toString() + " ║");
            System.out.println("╚═══╩═════════╩═══╝");
        }


    }
}
