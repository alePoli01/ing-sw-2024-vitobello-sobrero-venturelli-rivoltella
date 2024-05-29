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
    public int getPointsGiven(Board board, int X, int Y) {
        return switch (condition) {
            case QUILL -> board.getCollectedResources().get(Resource.QUILL) * pointsGiven;
            case MANUSCRIPT -> board.getCollectedResources().get(Resource.MANUSCRIPT) * pointsGiven;
            case INKWELL -> board.getCollectedResources().get(Resource.INKWELL) * pointsGiven;
            case EDGE -> board.surroundingCardsNumber(X, Y) * pointsGiven;
            case NULL -> pointsGiven;
        };
    }

    //TODO: controllare se si può spostare su una classe esterna
    public void linePrinter(int version, int line, boolean isFlipped) {
        String red = "\033[38;2;233;73;23m";   // Red
        String green = "\033[38;2;113;192;124m"; // green
        String blue = "\033[38;2;107;189;192m";  // Blue
        String magenta = "\033[38;2;171;63;148m";  // Magenta
        String gold = "\033[38;2;255;215;0m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String color = null;

        if (this.reign == Resource.ANIMAL) {
            color = blue;
            System.out.print(blue);
        }
        if (this.reign == Resource.FUNGI) {
            color = red;
            System.out.print(red);
        }
        if (this.reign == Resource.PLANT) {
            color = green;
            System.out.print(green);
        }
        if (this.reign == Resource.INSECT) {
            color = magenta;
            System.out.print(magenta);
        }

        if (this.serialNumber >= 41) {
            switch (version) {
                case (0): {//fullcard
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + reset + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                case (1): {//bottomleft not shown
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                case (2): {//bottomright not shown
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + "   ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                case (4): {//upleft not shown
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
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + "   ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
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
                                System.out.print("║ " + this.edgeResource[3].toString() + color + " ║   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + color + "   ║ " + this.edgeResource[2].toString() + color + " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+ "│" + this.reign.toString() +color+ "│   ╚═══╣");

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
                                System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
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
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + "   ");
                            } else {
                                System.out.print("         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign.toString() +color+ "│   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() + color + " ║ " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + color + " ║ " + this.edgeResource[1].toString() + color + " ║");
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
                case (9): {//allside not shown
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
                                System.out.print("   " + gold + this.pointsGiven + reset + color + "║" + gold + this.condition.toString() + reset + "   ");
                            } else {
                                System.out.print("         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ");

                            } else {
                                System.out.print("   "+reset+ "│" + this.reign.toString() +color+ "│   ");

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
                                System.out.print(" " + red + this.resourceNeeded.get(Resource.FUNGI) + reset + "│" + blue + this.resourceNeeded.get(Resource.ANIMAL) + reset + "│" + green + this.resourceNeeded.get(Resource.PLANT) + reset + "│" + magenta + this.resourceNeeded.get(Resource.INSECT) + reset + " ");
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
        }else{
            switch(version){
                case (0): {//fullcard
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ║ " + this.edgeResource[1].toString() +color+ " ║");
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("         ║ " + this.edgeResource[1].toString() +color+ " ║");
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ");
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ║ " + this.edgeResource[1].toString() +color+ " ║");
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
                case (4): {//upleft
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
                                System.out.print("    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ║ " + this.edgeResource[1].toString() +color+ " ║");
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
                case (5): {//leftside not shown
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
                                System.out.print("    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("         ╚═══╣");

                            } else {
                                System.out.print("   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("         ║ " + this.edgeResource[1].toString() +color+ " ║");
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ");
                            } else {
                                System.out.print("║   ║         ");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ");
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
                                System.out.print("║ " + this.edgeResource[3].toString() +color+ " ║    " + gold + this.pointsGiven + reset +color+ "    ║ " + this.edgeResource[2].toString() +color+ " ║");
                            } else {
                                System.out.print("║   ║         ║   ║");
                            }
                            break;
                        }
                        case (2): {//third line printed
                            if (!isFlipped) {
                                System.out.print("╠═══╝         ╚═══╣");

                            } else {
                                System.out.print("╠═══╝   "+reset+"│" + this.reign.toString() + "│"+color+"   ╚═══╣");

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
                                System.out.print("   "+reset+"│" + this.reign.toString() + "│"+color+"   ");

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
                                System.out.print("║ " + this.edgeResource[0].toString() +color+ " ║         ║ " + this.edgeResource[1].toString() +color+ " ║");
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

}

