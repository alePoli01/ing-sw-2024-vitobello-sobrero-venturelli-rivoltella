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
    public void cardPrinter(boolean isFlipped){

        //colors of the characters
        String red = "\u001b[31m";   // Red
        String green = "\u001b[32m"; // green
        String blue = "\u001b[36m";  // Blue
        String magenta = "\u001b[35m";  // Magenta
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String white = "\u001b[37m";

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
        String color=null;

        if(this.reign==Resource.ANIMAL){
            color=blue;
            System.out.print(blue);
        }
        if(this.reign==Resource.FUNGI){
            color=red;
            System.out.print(red);
        }
        if(this.reign==Resource.PLANT){
            color=green;
            System.out.print(green);
        }
        if(this.reign==Resource.INSECT){
            color=magenta;
            System.out.print(magenta);
        }
        if (this.serialNumber <= 40) {
            //resource card

            if (!isFlipped) {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                System.out.println("╠═══╝         ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║ " + this.edgeResource[0].toString() +color+ " ║         ║ " + this.edgeResource[1].toString() +color+ " ║");
                System.out.println("╚═══╩═════════╩═══╝");
            } else {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║   ║         ║   ║");
                System.out.println("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║   ║         ║   ║");
                System.out.println("╚═══╩═════════╩═══╝");
            }


        }
        if (this.serialNumber > 40 && this.serialNumber <= 80) {
            //gold card
            if (!isFlipped) {
                System.out.println("╔═══╦════╦════╦═══╗");
                System.out.println("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                System.out.println("╠═══╝         ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                System.out.println("╚═══╩═════════╩═══╝");
            } else {
                System.out.println("╔═══╦═════════╦═══╗");
                System.out.println("║   ║         ║   ║");
                System.out.println("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");
                System.out.println("╠═══╗         ╔═══╣");
                System.out.println("║   ║         ║   ║");
                System.out.println("╚═══╩═════════╩═══╝");
            }
        }

    }

    public void linePrinter(int version, int line, boolean isFlipped) {
        String red = "\u001b[31m";   // Red
        String green = "\u001b[32m"; // green
        String blue = "\u001b[36m";  // Blue
        String magenta = "\u001b[35m";  // Magenta
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String color=null;

        if(this.reign==Resource.ANIMAL){
            color=blue;
            System.out.print(blue);
        }
        if(this.reign==Resource.FUNGI){
            color=red;
            System.out.print(red);
        }
        if(this.reign==Resource.PLANT){
            color=green;
            System.out.print(green);
        }
        if(this.reign==Resource.INSECT){
            color=magenta;
            System.out.print(magenta);
        }
        switch(version){
            case(0): {//fullcard
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        } else {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+"║"+reset + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ╚═══╣");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════╩═══╝");
                        } else {
                            System.out.print("╚═══╩═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;
            }
            case(1):{//bottomleft not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        } else {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ╚═══╣");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("         ╔═══╣");
                        } else {
                            System.out.print("         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print(" "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("═════════╩═══╝");
                        } else {
                            System.out.print("═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;

            }
            case(2):{//bottomright not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        } else {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ╚═══╣");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ");
                        } else {
                            System.out.print("╠═══╗         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+" ");
                        } else {
                            System.out.print("║   ║         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════");
                        } else {
                            System.out.print("╚═══╩═════════");
                        }
                        break;
                    }
                }
                break;

            }
            case(3):{//upright not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════");
                        } else {
                            System.out.print("╔═══╦════╦════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset + color+ "║" + gold + this.condition.toString() + reset + "   ");
                        } else {
                            System.out.print("║   ║         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════╩═══╝");
                        } else {
                            System.out.print("╚═══╩═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;

            }
            case(4):{//upleft not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("════╦════╦═══╗");
                        } else {
                            System.out.print("════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("         ╚═══╣");

                        } else {
                            System.out.print("   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════╩═══╝");
                        } else {
                            System.out.print("╚═══╩═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;


            }
            case(5):{//left side not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("════╦════╦═══╗");
                        } else {
                            System.out.print("════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("         ╚═══╣");

                        } else {
                            System.out.print("   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("         ╔═══╣");
                        } else {
                            System.out.print("         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print(" "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("═════════╩═══╝");
                        } else {
                            System.out.print("═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;

            }
            case(6):{//right side not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════");
                        } else {
                            System.out.print("╔═══╦════╦════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset + "   ");
                        } else {
                            System.out.print("║   ║         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ");
                        } else {
                            System.out.print("╠═══╗         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+" ");
                        } else {
                            System.out.print("║   ║         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════");
                        } else {
                            System.out.print("╚═══╩═════════");
                        }
                        break;
                    }
                }
                break;


            }
            case(7):{//bottom side not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        } else {
                            System.out.print("╔═══╦════╦════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset +color+ "   ║ " + this.edgeResource[2].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╝         ╚═══╣");

                        } else {
                            System.out.print("╠═══╝   │" + this.reign.toString() + "│   ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("         ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print(" "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+" ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("═════════");
                        } else {
                            System.out.print("═════════");
                        }
                        break;
                    }
                }
                break;

            }
            case(8):{//upside not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("════╦════");
                        } else {
                            System.out.print("════╦════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("   " + gold + this.pointsGiven + reset+color+  "║" + gold + this.condition.toString() + reset + "   ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("         ");

                        } else {
                            System.out.print("   │" + this.reign.toString() + "│   ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║ "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+color+" ║ " + this.edgeResource[1].toString() +color+ " ║");
                        } else {
                            System.out.print("║   ║         ║   ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("╚═══╩═════════╩═══╝");
                        } else {
                            System.out.print("╚═══╩═════════╩═══╝");
                        }
                        break;
                    }
                }
                break;

            }
            case(9):{//allside not shown
                switch (line) {
                    case (0): {//first line printed
                        if (!isFlipped) {
                            System.out.print("════╦════");
                        } else {
                            System.out.print("════╦════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (!isFlipped) {
                            System.out.print("   " + gold + this.pointsGiven + reset +color+ "║" + gold + this.condition.toString() + reset + "   ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (!isFlipped) {
                            System.out.print("         ");

                        } else {
                            System.out.print("   │" + this.reign.toString() + "│   ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (!isFlipped) {
                            System.out.print("         ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (!isFlipped) {
                            System.out.print(" "+red+this.resourceNeeded.get(Resource.FUNGI)+reset+"│"+blue+this.resourceNeeded.get(Resource.ANIMAL)+reset+"│"+green+this.resourceNeeded.get(Resource.PLANT)+reset+"│"+magenta+this.resourceNeeded.get(Resource.INSECT)+reset+" ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (!isFlipped) {
                            System.out.print("═════════");
                        } else {
                            System.out.print("═════════");
                        }
                        break;
                    }
                }
                break;

            }

        }
        System.out.print(reset);
    }
}

