package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;

/**
 * Represents the starter card in the game, extending {@link PlayableCard}.
 */
public class StartCard extends PlayableCard {

    /**
     * Array of resources at the back reigns of the card.
     */
    public final Resource[] backReigns;

    /**
     * Array of resources at the edge of the card.
     */
    public final Resource[] edgeBackResource;

    /**
     * Constructs a {@code StartCard} object with specified parameters.
     *
     * @param serialNumber     the serial number of the card
     * @param reign            the reign resource of the card
     * @param cardType         the type of the card (<i>Resource</i>, <i>Gold</i>)
     * @param edgeResource     the resources at the edge of the card
     * @param resourceNeeded   the resources needed to obtain this card
     * @param pointsGiven      the points given by this card
     * @param condition        the condition for obtaining this card
     * @param backReigns       the array of resources at the back reigns of the card
     * @param edgeBackResource the array of resources at the edge of the back side of the card
     */
    public StartCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, Resource[] backReigns, Resource[] edgeBackResource) {
        super(serialNumber, reign, cardType, edgeResource, resourceNeeded, pointsGiven, condition);
        this.backReigns = backReigns;
        this.edgeBackResource = edgeBackResource;
    }

    @Override
    public void linePrinter(int version, int line, boolean back) {
        switch (version) {
            case (0): {//full card
                switch (line) {
                    case (0): {//first line printed
                        if (back) {
                            System.out.print("╔═══╦═════════╦═══╗");
                        } else {
                            System.out.print("╔═══╦═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("╠═══╝         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("╔═══╦═════════╦═══╗");
                        } else {
                            System.out.print("╔═══╦═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("╠═══╝         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("         ╔═══╣");
                        } else {
                            System.out.print("         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("╔═══╦═════════╦═══╗");
                        } else {
                            System.out.print("╔═══╦═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("╠═══╝         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ");
                        } else {
                            System.out.print("╠═══╗         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("╔═══╦═════════");
                        } else {
                            System.out.print("╔═══╦═════════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ");

                        } else {
                            System.out.print("╠═══╝         ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("═════════╦═══╗");
                        } else {
                            System.out.print("═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("═════════╦═══╗");
                        } else {
                            System.out.print("═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("         ╔═══╣");
                        } else {
                            System.out.print("         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("╔═══╦═════════");
                        } else {
                            System.out.print("╔═══╦═════════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ");

                        } else {
                            System.out.print("╠═══╝         ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ");
                        } else {
                            System.out.print("╠═══╗         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("╔═══╦═════════╦═══╗");
                        } else {
                            System.out.print("╔═══╦═════════╦═══╗");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[3].toString() + " ║         ║ " + this.edgeBackResource[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[3].toString() + " ║         ║ " + this.edgeFrontResource[2].toString() + " ║");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("╠═══╝  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ╚═══╣");

                        } else {
                            System.out.print("╠═══╝         ╚═══╣");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("         ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("         ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
                        if (back) {
                            System.out.print("═════════");
                        } else {
                            System.out.print("═════════");
                        }
                        break;
                    }
                    case (1): {//second line printed
                        if (back) {
                            System.out.print("         ");
                        } else {
                            System.out.print("         ");
                        }
                        break;
                    }
                    case (2): {//third line printed
                        if (back) {
                            System.out.print("  ║" + this.backReigns[0].toString() + this.backReigns[1].toString() + this.backReigns[2].toString() + "║  ");

                        } else {
                            System.out.print("         ");

                        }
                        break;
                    }
                    case (3): {//fourth line printed
                        if (back) {
                            System.out.print("╠═══╗         ╔═══╣");
                        } else {
                            System.out.print("╠═══╗         ╔═══╣");
                        }
                        break;
                    }
                    case (4): {//fifth line printed
                        if (back) {
                            System.out.print("║ " + this.edgeBackResource[0].toString() + " ║         ║ " + this.edgeBackResource[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeFrontResource[0].toString() + " ║         ║ " + this.edgeFrontResource[1].toString() + " ║");
                        }
                        break;
                    }
                    case (5): {//last line printed
                        if (back) {
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
    }
}


