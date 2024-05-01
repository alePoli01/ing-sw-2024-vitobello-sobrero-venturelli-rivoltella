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

        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters
        String blue = "\u001b[36m";  // Blue

        //print of x-axis
        System.out.print(" ┃  ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("━━━━"+gold+j+reset+"━━━━  ┃ ");
        }
        System.out.println();

        //print of y-axis
        System.out.print(gold+(x_min-3)%10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min-3)%10+reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold+(x_min-2)/10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min-2)/10+reset);
        System.out.println();

        System.out.print(gold+(x_min-2)%10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min-2)%10+reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold+(x_min-1)/10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min-1)/10+reset);
        System.out.println();

        //starts the print of the board
        for (int i = x_min; i <= x_max; i = i + 2) {

            for (int line = 0; line <= 5; line++) {

                if(line==0){
                    System.out.print(gold+(i-1)%10+reset);
                }
                if(line==1||line==4){
                    System.out.print("╪");
                }
                if(line==2){
                    System.out.print(gold+i/10+reset);
                }
                if(line==3){
                    System.out.print(gold+i%10+reset);
                }
                if(line==5){
                    System.out.print(gold+(i+1)/10+reset);
                }
                System.out.print("             ");

                for (int j = y_min; j <= y_max; j = j + 2) {
                    //no card
                    if (Board[i][j] == null) {
                        System.out.print("         ");
                    }else{

                    //fullcard
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                        Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);
                        if(j!=y_max){
                        System.out.print("         ");}

                    }
                    //bottomleft
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                        if (Board[i][j].weight > Board[i + 1][j - 1].weight) {

                            Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);

                        }else{
                            Board[i][j].getCardPointer().lineprinter(1, line, Board[i][j].isFlipped);
                        }

                    }
                    //bottomright
                    if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                        if (Board[i][j].weight > Board[i + 1][j + 1].weight) {
                            Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);

                            if (line > 2) {
                                if(Board[i][j + 2]!=null&&(Board[i+1][j+1].weight > Board[i][j + 2].weight)){
                                    Board[i + 1][j + 1].getCardPointer().lineprinter(4, line, Board[i + 1][j + 1].isFlipped);
                                }else{
                                    Board[i + 1][j + 1].getCardPointer().lineprinter(8, line, Board[i + 1][j + 1].isFlipped);
                                }

                            }else{
                                System.out.print("         ");
                            }
                        } else {
                            Board[i][j].getCardPointer().lineprinter(2, line, Board[i][j].isFlipped);
                            if (line > 2) {
                                Board[i + 1][j + 1].getCardPointer().lineprinter(0, line - 3, Board[i + 1][j + 1].isFlipped);
                            }else{
                                System.out.print("         ");
                            }
                        }

                    }
                    //upright
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                        if (Board[i][j].weight > Board[i - 1][j + 1].weight) {
                            Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);

                            if (line <= 2) {
                                if(Board[i][j + 2]!=null&&(Board[i-1][j+1].weight > Board[i][j + 2].weight)){
                                    Board[i - 1][j + 1].getCardPointer().lineprinter(1, line+3, Board[i - 1][j + 1].isFlipped);
                                }else{
                                    Board[i - 1][j + 1].getCardPointer().lineprinter(7, line+3, Board[i - 1][j + 1].isFlipped);
                                }

                            }else{
                                System.out.print("         ");
                            }
                        } else {
                            Board[i][j].getCardPointer().lineprinter(3, line, Board[i][j].isFlipped);
                            if (line <= 2) {
                                Board[i - 1][j + 1].getCardPointer().lineprinter(0, line + 3, Board[i - 1][j + 1].isFlipped);
                            }else{
                                System.out.print("         ");
                            }
                        }

                    }
                    //upleft
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] == null) {
                        if (Board[i][j].weight > Board[i - 1][j - 1].weight) {
                            Board[i][j].getCardPointer().lineprinter(0, line, Board[i][j].isFlipped);
                        } else {
                            Board[i][j].getCardPointer().lineprinter(4, line, Board[i][j].isFlipped);
                        }


                    }
                    //leftside
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                        Board[i][j].getCardPointer().lineprinter(5, line, Board[i][j].isFlipped);

                    }
                    //Rightside
                    if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                        Board[i][j].getCardPointer().lineprinter(6, line, Board[i][j].isFlipped);

                    }
                    //bottom side
                    if (Board[i + 1][j + 1] == null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] == null && Board[i - 1][j + 1] != null) {
                        Board[i][j].getCardPointer().lineprinter(7, line, Board[i][j].isFlipped);

                    }
                    //upside
                    if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] == null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] == null) {
                        Board[i][j].getCardPointer().lineprinter(8, line, Board[i][j].isFlipped);

                    }
                    //no sides
                    if (Board[i + 1][j + 1] != null && Board[i - 1][j - 1] != null && Board[i + 1][j - 1] != null && Board[i - 1][j + 1] != null) {
                        Board[i][j].getCardPointer().lineprinter(9, line, Board[i][j].isFlipped);

                    }}

                }
                System.out.print("             ");

                if(line==0){
                    System.out.print(gold+(i-1)%10+reset);
                }
                if(line==1||line==4){
                    System.out.print("╪");
                }
                if(line==2){
                    System.out.print(gold+i/10+reset);
                }
                if(line==3){
                    System.out.print(gold+i%10+reset);
                }
                if(line==5){
                    System.out.print(gold+(i+1)/10+reset);
                }
                System.out.println();
            }
        }

        System.out.print(gold+(x_min+1)%10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min+1)%10+reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold+(x_min+2)/10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min+2)/10+reset);
        System.out.println();

        System.out.print(gold+(x_min+2)%10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min+2)%10+reset);
        System.out.println();

        System.out.print("╪   ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print("╪");
        System.out.println();

        System.out.print(gold+(x_min+3)/10+"   "+reset);
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("              ");
        }
        System.out.print(gold+(x_min+3)/10+reset);
        System.out.println();

        //print of x-axis
        System.out.print(" ┃  ");
        for (int j = y_min-1; j <= y_max+1; j++){
            System.out.print("━━━━"+gold+j+reset+"━━━━  ┃ ");
        }
        System.out.println();
    }
}

