package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;

public class StartCard extends PlayableCard {
    public final Resource[] backReigns;
    public final Resource[] reignBackPointEdge;


    public StartCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, Resource[] backReigns, Resource[] reignBackPointEdge) {
        super(serialNumber, reign, cardType, edgeResource, resourceNeeded, pointsGiven, condition);
        this.backReigns = backReigns;
        this.reignBackPointEdge = reignBackPointEdge;
    }

    @Override
    public void linePrinter(int version, int line, boolean back) {
        switch (version) {
            case (0): {//fullcard
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ║ " + this.edgeResource[1].toString() + " ║");
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
            case (1): {//bottomleft not shown
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeResource[1].toString() + " ║");
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
            case (2): {//bottomright not shown
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ");
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ║ " + this.edgeResource[1].toString() + " ║");
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
            case (4): {//upleft not shown
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
                            System.out.print("         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ║ " + this.edgeResource[1].toString() + " ║");
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
                            System.out.print("         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("         ║ " + this.edgeResource[1].toString() + " ║");
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ");
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
                            System.out.print("║ " + this.reignBackPointEdge[3].toString() + " ║         ║ " + this.reignBackPointEdge[2].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[3].toString() + " ║         ║ " + this.edgeResource[2].toString() + " ║");
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
                            System.out.print("║ " + this.reignBackPointEdge[0].toString() + " ║         ║ " + this.reignBackPointEdge[1].toString() + " ║");
                        } else {
                            System.out.print("║ " + this.edgeResource[0].toString() + " ║         ║ " + this.edgeResource[1].toString() + " ║");
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


