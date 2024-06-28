package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.io.Serializable;
import java.util.Map;


/**
 * Represents a playable card in the game. Each card has a serial number, reign, type, resources on the edges,
 * resources needed to play, points given under certain conditions, and specific points condition.
 */
public class PlayableCard implements Serializable {

    /** The serial number of the card. */
    public final int serialNumber;

    /** The reign resource associated with the card. */
    public final Resource reign;

    /** The type of the card (<i>Resource</i>, <i>Gold</i> or <i>Starter</i>). */
    public final CardType cardType;

    /** The resources on the edges of the card. */
    public final Resource[] edgeFrontResource;

    /** The resources needed to play the card and their quantities. */
    public final Map<Resource, Integer> resourceNeeded;

    /** The points given by the card. */
    public final int pointsGiven;

    /** The condition under which points are given. */
    public final PointsCondition condition;


    private final String red = "\033[38;2;233;73;23m";   // Red
    private final String green = "\033[38;2;113;192;124m"; // green
    private final String blue = "\033[38;2;107;189;192m";  // Blue
    private final String magenta = "\033[38;2;171;63;148m";  // Magenta
    private final String gold = "\033[38;2;255;215;0m";  // gold
    private final String reset = "\u001b[0m";  // reset color of the characters


    /**
     * Constructs a new {@code PlayableCard} with the given attributes.
     *
     * @param serialNumber      The serial number of the card.
     * @param reign             The reign resource associated with the card.
     * @param cardType          The type of the card.
     * @param edgeFrontResource The resources on the edges of the card.
     * @param resourceNeeded    The resources needed to play the card and their quantities.
     * @param pointsGiven       The points given by the card.
     * @param condition         The condition under which points are given.
     */
    public PlayableCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeFrontResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition) {
        this.serialNumber = serialNumber;
        this.reign = reign;
        this.cardType = cardType;
        this.edgeFrontResource = edgeFrontResource;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
        this.condition = condition;
    }

    /**
     * Calculates the points given after the card is placed on the board based on the specified condition.
     *
     * @param board The game board where the card is placed.
     * @param X     The X coordinate of the card placement.
     * @param Y     The Y coordinate of the card placement.
     * @return The points given based on the condition.
     */
    public int getPointsGiven(Board board, int X, int Y) {
        return switch (condition) {
            case QUILL -> board.getCollectedResources().get(Resource.QUILL) * pointsGiven;
            case MANUSCRIPT -> board.getCollectedResources().get(Resource.MANUSCRIPT) * pointsGiven;
            case INKWELL -> board.getCollectedResources().get(Resource.INKWELL) * pointsGiven;
            case EDGE -> board.surroundingCardsNumber(X, Y) * pointsGiven;
            case NULL -> pointsGiven;
        };
    }

    public void linePrinter(int version, int line, boolean isFlipped) {
        String color;

        switch(this.reign){
            case FUNGI  ->  color=red;
            case ANIMAL ->  color=blue;
            case PLANT  ->  color=green;
            case INSECT ->  color=magenta;
            default     ->  color=reset;
        }
        System.out.print(color);

        if (this.serialNumber >= 41) {
            switch (version) {
                case (0): {//full card
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3] + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + reset + gold + this.condition  + reset + color + "   ║ " + this.edgeFrontResource[2]  + color + " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                            printGoldCardRequirementsWithBothEdges(isFlipped, color);
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
                case (1): {//bottom left not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            printGoldCardInfo(isFlipped,color);
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                            printGoldCardRequirementsWithEdgeR(isFlipped, color);
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
                case (2): {//bottom right not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            printGoldCardInfo(isFlipped, color);
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                            printGoldCardRequirementsWithEdgeL(isFlipped, color);
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
                case (3): {//upright not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════");
                            } else {
                                System.out.print("╔═══╦═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3] + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition  + reset + "   ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ");

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
                            printGoldCardRequirementsWithBothEdges(isFlipped, color);
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
                case (4): {//up left not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════╦═══╗");
                            } else {
                                System.out.print("═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition + reset + color + "   ║ " + this.edgeFrontResource[2]  + color + " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                                System.out.print("║ " + this.edgeFrontResource[0] + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeFrontResource[1]  + color + " ║");
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
                case (5): {//left side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════╦═══╗");
                            } else {
                                System.out.print("═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition + reset + color + "   ║ " + this.edgeFrontResource[2]  + color + " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                            printGoldCardRequirementsWithEdgeR(isFlipped, color);
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
                case (6): {//right side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════");
                            } else {
                                System.out.print("╔═══╦═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]+ color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition  + reset + "   ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ");

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
                            printGoldCardRequirementsWithEdgeL(isFlipped, color);
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
                case (7): {//bottom side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            printGoldCardInfo(isFlipped, color);
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign  +color+ "│   ╚═══╣");

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
                            printGoldCardRequirements(isFlipped);
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
                case (8): {//upside not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════");
                            } else {
                                System.out.print("═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition  + reset + "   ");
                            } else {
                                System.out.print("         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign  +color+ "│   ");

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
                            printGoldCardRequirementsWithBothEdges(isFlipped, color);
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
                case (9): {//all side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════");
                            } else {
                                System.out.print("═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition  + reset + "   ");
                            } else {
                                System.out.print("         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign  +color+ "│   ");

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
                            printGoldCardRequirements(isFlipped);
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
        }else{
            switch(version){
                case (0): {//full card
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3] +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeFrontResource[0] +color+ " ║         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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

                case (1): {//bottom left not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]  +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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
                case (2): {//bottom right not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]  +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeFrontResource[0]  +color+ " ║         ");
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
                case (3): {//upright not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════");
                            } else {
                                System.out.print("╔═══╦═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]  +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeFrontResource[0]  +color+ " ║         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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
                case (4): {//up left
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════╦═══╗");
                            } else {
                                System.out.print("═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeFrontResource[0]  +color+ " ║         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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
                case (5): {//left side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════╦═══╗");
                            } else {
                                System.out.print("═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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
                case (6): {//right side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════");
                            } else {
                                System.out.print("╔═══╦═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]  +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeFrontResource[0]  +color+ " ║         ");
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
                case (7): {//bottom side not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("╔═══╦═════════╦═══╗");
                            } else {
                                System.out.print("╔═══╦═════════╦═══╗");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("║ " + this.edgeFrontResource[3]  +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeFrontResource[2]  +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign  + "│"+color+"   ╚═══╣");

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
                                System.out.print("         ");
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
                case (8): {//upside not shown
                    switch (line) {
                        case (0): {//first line printed
                            if (!isFlipped) {
                                System.out.print("═════════");
                            } else {
                                System.out.print("═════════");
                            }
                            break;
                        }
                        case (1): {//second line printed
                            if (!isFlipped) {
                                System.out.print("    " + gold + this.pointsGiven + reset +color+ "    ");
                            } else {
                                System.out.print("         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ");

                            } else {
                                System.out.print("   "+reset+"│" + this.reign  + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeFrontResource[0]  +color+ " ║         ║ " + this.edgeFrontResource[1]  +color+ " ║");
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
            }
            System.out.print(reset);

        }
    }

    /**
     * Prints the requirements and information of a gold card, formatted in a specific way.
     *
     * @param isFlipped Whether the card is flipped or not.
     * @param color     The color to be used for printing.
     */
    private void printGoldCardRequirementsWithEdgeL(boolean isFlipped, String color) {
        if (!isFlipped) {
            System.out.print("║ " + this.edgeFrontResource[0]  + color + " ║ "+reset + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
        } else {
            System.out.print("║   ║         ");
        }
    }

    /**
     * Prints the requirements and information of a gold card, formatted in a specific way.
     *
     * @param isFlipped Whether the card is flipped or not.
     * @param color     The color to be used for printing.
     */
    private void printGoldCardRequirementsWithEdgeR(boolean isFlipped, String color) {
        if (!isFlipped) {
            System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeFrontResource[1]  + color + " ║");
        } else {
            System.out.print("         ║   ║");
        }
    }

    /**
     * Prints the requirements of the card for a gold card, formatted in a specific way.
     *
     * @param isFlipped Whether the card is flipped or not.
     */
    private void printGoldCardRequirements(boolean isFlipped) {
        if (!isFlipped) {
            System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
        } else {
            System.out.print("         ");
        }

    }

    /**
     * Prints the information of the card for a gold card, formatted in a specific way.
     *
     * @param isFlipped Whether the card is flipped or not.
     * @param color     The color to be used for printing.
     */
    private void printGoldCardInfo(boolean isFlipped, String color) {
        if (!isFlipped) {
            System.out.print("║ " + this.edgeFrontResource[3]  + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition  + reset + color + "   ║ " + this. edgeFrontResource[2]  + color + " ║");
        } else {
            System.out.print("║   ║         ║   ║");
        }

    }


    /**
     * Prints the information of the card for a gold card, formatted in a specific way.
     *
     * @param isFlipped Whether the card is flipped or not.
     * @param color     The color to be used for printing.
     */
    private void printGoldCardRequirementsWithBothEdges(boolean isFlipped, String color) {
        if (!isFlipped) {
            System.out.print("║ " + this.edgeFrontResource[0]  + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this. edgeFrontResource[1]  + color + " ║");
        } else {
            System.out.print("║   ║         ║   ║");
        }
    }
}