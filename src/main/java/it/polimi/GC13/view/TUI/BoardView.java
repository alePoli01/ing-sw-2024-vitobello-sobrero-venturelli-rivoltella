package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Cell;
import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.StartCard;

public class BoardView {
    public Cell[][] Board;
    public int x_max, x_min, y_max, y_min;

    public BoardView() {
        Board = new Cell[150][150];
        x_max = 50;
        y_max = 50;
        x_min = 50;
        y_min = 50;
    }

    public void InsertCard(int y, int x, int serialnumber, int z, Deck deck, boolean isflipped) {
        for (PlayableCard card : deck.getResourceDeck()) {
            if (serialnumber == card.serialNumber) {
                this.Board[y][x] = new Cell(card, z, isflipped);
            }
        }
        for (PlayableCard card : deck.getGoldDeck()) {
            if (serialnumber == card.serialNumber) {
                this.Board[y][x] = new Cell(card, z, isflipped);
            }
        }
        for (StartCard card : deck.getStartDeck()) {
            if (serialnumber == card.serialNumber) {
                this.Board[y][x] = new Cell(card, z, isflipped);
            }
        }
        if (y > x_max) {
            x_max = y;
        }
        if (x > y_max) {
            y_max = x;
        }
        if (y < x_min) {
            x_min = y;
        }
        if (x < y_min) {
            y_min = x;
        }
        if(y_min%2!=0&&x_min%2==0){
            y_min--;
            y_max=y_max+2;
        }
    }

    public void printBoard() {

        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String blue = "\u001b[36m";  // Blue

        //print of x-axis
        System.out.print(" X┃ ");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if (j == y_max + 1) {
                System.out.print("━━━━" + gold + j + reset + "━━━━  ┃X");
            } else {
                System.out.print("━━━━" + gold + j + reset + "━━━━  ┃ ");
            }
        }
        System.out.println();

        //print of y-axis
        System.out.print("Y   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }

        }
        System.out.print("Y");
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("─ ─ ─ ─ ─ ─ | ");
            }else{
                System.out.print("─ ─ ─ ─ ─ ─   ");
            }
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold + (x_min - 2) / 10 + "   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }
        }
        System.out.print(gold + (x_min - 2) / 10 + reset);
        System.out.println();

        System.out.print(gold + (x_min - 2) % 10 + "   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }
        }
        System.out.print(gold + (x_min - 2) % 10 + reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("─ ─ ─ ─ ─ ─ | ");
            }else{
                System.out.print("─ ─ ─ ─ ─ ─   ");
            }
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold + (x_min - 1) / 10 + "   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }
        }
        System.out.print(gold + (x_min - 1) / 10 + reset);
        System.out.println();

        //starts the print of the board
        for (int i = x_min; i <= x_max; i = i + 2) {

            for (int line = 0; line <= 5; line++) {

                if (line == 0) {
                    System.out.print(gold + (i - 1) % 10 + reset);
                }
                if (line == 1 || line == 4) {
                    System.out.print("╪");
                }
                if (line == 2) {
                    System.out.print(gold + i / 10 + reset);
                }
                if (line == 3) {
                    System.out.print(gold + i % 10 + reset);
                }
                if (line == 5) {
                    System.out.print(gold + (i + 1) / 10 + reset);
                }
                if(line==1||line==4){
                    System.out.print("   ─ ─ ─ ─ ─ ");
                }else{
                    System.out.print("             ");
                }

                for (int j = y_min; j <= y_max; j = j + 2) {
                    //no card
                    if (Board[i][j] == null) {
                        if (line <= 2) {
                            if (Board[i - 1][j - 1] == null && Board[i - 1][j + 1] == null && false) {
                                System.out.print("c                 d");
                            } else {
                                if (Board[i - 1][j - 1] != null) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }

                                } else {
                                    if(line==1||line==4){
                                        System.out.print("─ | ─ ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("  |           ");
                                    }

                                }
                            }
                        } else {
                            if (Board[i + 1][j - 1] == null && Board[i + 1][j + 1] == null && false) {
                                System.out.print("c                 d");
                            } else {
                                if (Board[i + 1][j - 1] != null) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }

                                } else {
                                    if(line==1||line==4){
                                        System.out.print("─ | ─ ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("  |           ");
                                    }
                                }
                            }
                        }
                        if (line <= 2) {
                            if (Board[i - 1][j + 1] != null) {
                                //check what card to print
                                if (Board[i][j + 2] != null) {
                                    if (Board[i][j + 2].weight < Board[i - 1][j + 1].weight) {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                } else {
                                    Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                }
                            } else {
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print("─ | ─ ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("  |           ");
                                    }
                                }

                            }
                        } else {
                            if (Board[i + 1][j + 1] != null) {
                                //check what card to print
                                if (Board[i][j + 2] != null) {
                                    if (Board[i][j + 2].weight < Board[i + 1][j + 1].weight) {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else {
                                    Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                }
                            } else {
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print("─ | ─ ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("  |           ");
                                    }
                                }

                            }
                        }
                    } else {

                        //fullcard
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                            Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                            if (j != y_max) {
                                if(line==1||line==4){
                                    System.out.print(" ─ ─ ─ ─ ");
                                }else{
                                    System.out.print("         ");
                                }
                            }

                        }
                        //bottomleft
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                            if (Board[i][j].weight > Board[i + 1][j - 1].weight) {

                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }

                            } else {
                                Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            }


                        }
                        //bottomright
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                            if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                if (line > 2) {
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }

                                } else {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            } else {
                                Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                if (line > 2) {
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            }

                        }
                        //upright
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                            if (Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                if (line <= 2) {
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    }else{
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);

                                    }
                                }else {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            } else {
                                Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                if (line <= 2) {
                                    if (Board[i][j + 2] != null) {
                                        if (Board[i - 1][j + 1].weight > Board[i][j + 2].weight) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }

                                } else {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            }

                        }
                        //upleft
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                            if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            } else {
                                Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            }


                        }
                        //leftside
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                            if (line <= 2) {
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }
                            } else {
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            }

                        }
                        //Rightside
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                            if (line <= 2) {
                                if (Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                            } else {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null) {
                                            if (Board[i - 1][j + 1].weight > Board[i][j + 2].weight) {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                            } else {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }

                                    } else {

                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }
                            } else {

                                if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }

                        }
                        //bottom side
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                            if (line <= 2) {
                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }
                            } else {
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(7, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                }
                            }
                        }
                        //upside
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                            if (line <= 2) {
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(8, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }

                            }else{
                                Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                if (j != y_max) {
                                    if(line==1||line==4){
                                        System.out.print(" ─ ─ ─ ─ ");
                                    }else{
                                        System.out.print("         ");
                                    }
                                }

                            }
                        }
                        //card missing on bottom right
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null) {
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(8, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }
                            }else{
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }

                                } else {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }

                        }
                        //card missing on up left
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null) {
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null && (Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null) {
                                            if (Board[i - 1][j + 1].weight > Board[i][j + 2].weight) {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                            } else {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }else{
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(7, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                }
                            }

                        }
                        //card missing on bottom left
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(8, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }
                            }else{
                                if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }
                            }


                        }
                        //card missing on up right
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }else{
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(7, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                }
                            }


                        }
                        //two cards on the diagonal going down
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null){
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }


                            }else{
                                if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (line > 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                            } else {
                                                Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }
                            }

                        }
                        //two cards on the diagonal going up
                        if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null){
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null) {
                                            if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                            } else {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                            }
                                        }else{
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);

                                        }
                                    }else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                } else {
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (line <= 2) {
                                        if (Board[i][j + 2] != null) {
                                            if (Board[i - 1][j + 1].weight > Board[i][j + 2].weight) {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                            } else {
                                                Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                            }
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }

                                    } else {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }else{
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }

                                } else {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (j != y_max) {
                                        if(line==1||line==4){
                                            System.out.print(" ─ ─ ─ ─ ");
                                        }else{
                                            System.out.print("         ");
                                        }
                                    }
                                }

                            }

                        }
                        //no sides
                        if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null) {
                            if(line<=2){
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight > Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(4, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(7, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(8, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(2, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i - 1][j - 1].weight && Board[i][j].weight < Board[i - 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(3, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i - 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                        } else {
                                            Board[i - 1][j + 1].getCardPointer().linePrinter(1, line + 3, Board[i - 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i - 1][j + 1].getCardPointer().linePrinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                                    }
                                }

                            }else{
                                if (Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {

                                    Board[i][j].getCardPointer().linePrinter(0, line, Board[i][j].isFlipped);

                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(8, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(4, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(1, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else if (Board[i][j].weight < Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().linePrinter(7, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }

                                }else if(Board[i][j].weight > Board[i + 1][j - 1].weight && Board[i][j].weight < Board[i + 1][j + 1].weight){
                                    Board[i][j].getCardPointer().linePrinter(2, line, Board[i][j].isFlipped);
                                    if (Board[i][j + 2] != null) {
                                        if ((Board[i + 1][j + 1].weight > Board[i][j + 2].weight)) {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                        } else {
                                            Board[i + 1][j + 1].getCardPointer().linePrinter(3, line - 3, Board[i + 1][j + 1].isFlipped);
                                        }
                                    } else {
                                        Board[i + 1][j + 1].getCardPointer().linePrinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                }

                            }


                        }
                    }

                    if (j == y_max && Board[i][j] == null) {
                        if(line==1||line==4){
                            System.out.print("─ | ─");
                        }else{
                            System.out.print("  |  ");
                        }
                    }
                    if (line <= 2) {
                        if (j == y_max - 1 && (j + 1) % 2 != (y_min % 2) && Board[i - 1][j + 1] == null) {
                            if(line==1||line==4){
                                System.out.print("─ | ─");
                            }else{
                                System.out.print("  |  ");
                            }
                        }
                    } else {
                        if (j == y_max - 1 && (j + 1) % 2 != (y_min % 2) && Board[i + 1][j + 1] == null) {
                            if(line==1||line==4){
                                System.out.print("─ | ─");
                            }else{
                                System.out.print("  |  ");
                            }
                        }
                    }

                }
                if(line==1||line==4){
                    System.out.print(" ─ ─ ─ ─ ─   ");
                }else{
                    System.out.print("             ");
                }


                if (line == 0) {
                    System.out.print(gold + (i - 1) % 10 + reset);
                }
                if (line == 1 || line == 4) {
                    System.out.print("╪");
                }
                if (line == 2) {
                    System.out.print(gold + i / 10 + reset);
                }
                if (line == 3) {
                    System.out.print(gold + i % 10 + reset);
                }
                if (line == 5) {
                    System.out.print(gold + (i + 1) / 10 + reset);
                }
                System.out.println();
            }
        }

        System.out.print(gold + (x_max + 1) % 10 + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if (j % 2 != y_min % 2) {
                if (j == y_min - 1) {
                    System.out.print("               |  ");
                } else {
                    if (Board[x_max][j] != null) {
                        Board[x_max][j].getCardPointer().linePrinter(0, 3, Board[x_max][j].isFlipped);
                    } else {
                        if (j == y_max+1) {
                            System.out.print("   |               ");
                        }else{
                            System.out.print("  |             |  ");
                        }

                    }

                }

            } else {
                if (j == y_max) {
                    System.out.print("        ");
                } else {
                    System.out.print("         ");
                }

            }
        }
        System.out.print(gold + (x_max + 1) % 10 + reset);
        System.out.println();

        System.out.print("╪");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if (j % 2 != y_min % 2) {

                if (j == y_min - 1) {
                        System.out.print("   ─ ─ ─ ─ ─ ─ | ─");
                } else {
                    if (Board[x_max][j] != null) {
                        Board[x_max][j].getCardPointer().linePrinter(0, 4, Board[x_max][j].isFlipped);
                    } else {
                        if(j==y_max+1){
                            System.out.print(" ─ | ─ ─ ─ ─ ─ ─   ");
                        }else{
                            System.out.print("─ | ─ ─ ─ ─ ─ ─ | ─");
                        }

                    }

                }

            } else {
                if (j == y_max) {
                    System.out.print(" ─ ─ ─ ─");
                } else {
                    System.out.print(" ─ ─ ─ ─ ");
                }

            }

        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold + (x_max + 2) / 10 + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if (j % 2 != y_min % 2) {
                if (j == y_min - 1) {
                    System.out.print("               |  ");
                } else {
                    if (Board[x_max][j] != null) {
                        Board[x_max][j].getCardPointer().linePrinter(0, 5, Board[x_max][j].isFlipped);
                    } else {
                        if(j==y_max+1){
                            System.out.print("   |               ");
                        }else{
                            System.out.print("  |             |  ");
                        }
                    }

                }

            } else {
                if (j == y_max) {
                    System.out.print("        ");
                } else {
                    System.out.print("         ");
                }

            }
        }
        System.out.print(gold + (x_max + 2) / 10 + reset);
        System.out.println();

        System.out.print(gold + (x_max + 2) % 10 + "   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }
        }
        System.out.print(gold + (x_max + 2) % 10 + reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j==y_max+1){
                System.out.print("─ ─ ─ ─ ─ ─   ");
            }else{
                System.out.print("─ ─ ─ ─ ─ ─ | ");
            }
        }
        System.out.print("╪");
        System.out.println();

        System.out.print("Y   " + reset);
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if(j!=y_max+1){
                System.out.print("            | ");
            }else{
                System.out.print("              ");
            }
        }
        System.out.print("Y");
        System.out.println();

        //print of x-axis
        System.out.print(" X┃ ");
        for (int j = y_min - 1; j <= y_max + 1; j++) {
            if (j == y_max + 1) {
                System.out.print("━━━━" + gold + j + reset + "━━━━  ┃X");
            } else {
                System.out.print("━━━━" + gold + j + reset + "━━━━  ┃ ");
            }

        }
        System.out.println();
    }
}


