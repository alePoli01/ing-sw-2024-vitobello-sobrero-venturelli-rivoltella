package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;

public class StartCard extends PlayableCard {
    public final Resource[] backReigns;
    public final Resource[] edgeBackResource;


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


