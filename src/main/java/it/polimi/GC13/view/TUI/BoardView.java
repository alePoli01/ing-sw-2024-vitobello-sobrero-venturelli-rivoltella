package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Cell;
import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.StartCard;

public class BoardView {
    public Cell[][] Board;
    public int x_max,x_min,y_max,y_min;

    public BoardView(){
        Board=new Cell[80][80];
        x_max=50;
        y_max=50;
        x_min=50;
        y_min=50;
    }

    public void InsertCard(int x, int y, int serialnumber,int z, Deck deck,boolean isflipped){
        for(PlayableCard card: deck.getResourceDeck()){
            if(serialnumber== card.serialNumber){
                this.Board[x][y]= new Cell(card,z,isflipped) ;
            }
        }
        for(PlayableCard card: deck.getGoldDeck()){
            if(serialnumber== card.serialNumber){
                this.Board[x][y]= new Cell(card,z,isflipped) ;
            }
        }
        for(StartCard card: deck.getStartDeck()){
            if(serialnumber== card.serialNumber){
                this.Board[x][y]= new Cell(card,z,isflipped) ;
            }
        }
        if(x>x_max){
            x_max=x;
        }
        if(y>y_max){
            y_max=y;
        }
        if(x<x_min){
            x_min=x;
        }
        if(y<y_min){
            y_min=y;
        }

    }

    public void printBoard() {
        for (int i = x_min; i <= x_max; i++) {
            for (int line = 0; line <= 5; line++) {
                for (int j = y_min; j <= y_max; j++) {
                    if (Board[i][j] != null) {
                        if (i % 2 == 0) {
                            //fullcard
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                                Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);

                            }
                            //bottomleft
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                                Board[i][j].getCardPointer().lineprinter(1, line, Board[i][j].isFlipped);

                            }
                            //bottomright
                            if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                                if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                                    Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);
                                    if (line > 2) {
                                        Board[i + 1][j + 1].getCardPointer().lineprinter(4, line, Board[i + 1][j + 1].isFlipped);
                                    }
                                } else {
                                    Board[i][j].getCardPointer().lineprinter(2, line, Board[i][j].isFlipped);
                                    if (line > 2) {
                                        Board[i + 1][j + 1].getCardPointer().lineprinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                                    }
                                }

                            }
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                                Board[i][j].getCardPointer().lineprinter(3, line, Board[i][j].isFlipped);

                            }
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                                if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                                    Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);
                                } else {
                                    Board[i][j].getCardPointer().lineprinter(4, line, Board[i][j].isFlipped);
                                }


                            }
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                                Board[i][j].getCardPointer().lineprinter(5, line, Board[i][j].isFlipped);

                            }
                            if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                                Board[i][j].getCardPointer().lineprinter(6, line, Board[i][j].isFlipped);

                            }
                            if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                                Board[i][j].getCardPointer().lineprinter(7, line, Board[i][j].isFlipped);

                            }
                            if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                                Board[i][j].getCardPointer().lineprinter(8, line, Board[i][j].isFlipped);

                            }
                            if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null) {
                                Board[i][j].getCardPointer().lineprinter(9, line, Board[i][j].isFlipped);

                            }
                        }
                    }

                }
               System.out.println();
            }
        }
    }
}
