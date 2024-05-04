package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import junit.framework.TestCase;

public class BoardViewTest extends TestCase {

    public void testPrintBoard() {
        Deck deck=new Deck();
        BoardView board=new BoardView();

        //board.InsertCard(49,49,42,4,deck,true);
        //board.InsertCard(51,51,42,1,deck,false);
        //two card with a space between
        board.InsertCard(50,50,43,0,deck,false);
        board.InsertCard(50,52,43,1,deck,false);

        //two card with one car between, all cases
        board.InsertCard(54,50,43,0,deck,false);
        board.InsertCard(53,51,43,2,deck,false);
        board.InsertCard(54,52,43,1,deck,false);

        board.InsertCard(58,50,43,0,deck,false);
        board.InsertCard(57,51,43,1,deck,false);
        board.InsertCard(58,52,43,2,deck,false);

        board.InsertCard(62,50,43,1,deck,false);
        board.InsertCard(61,51,43,0,deck,false);
        board.InsertCard(62,52,43,2,deck,false);

        board.InsertCard(66,50,43,0,deck,false);
        board.InsertCard(67,51,43,2,deck,false);
        board.InsertCard(66,52,43,1,deck,false);

        board.InsertCard(70,50,43,0,deck,false);
        board.InsertCard(71,51,43,1,deck,false);
        board.InsertCard(70,52,43,2,deck,false);

        board.InsertCard(74,50,43,1,deck,false);
        board.InsertCard(75,51,43,0,deck,false);
        board.InsertCard(74,52,43,2,deck,false);
        //board.InsertCard(52,52,43,2,deck,false);
        //board.InsertCard(52,50,43,3,deck,true);
        //board.InsertCard(52,52,43,4,deck,true);
        board.printBoard();

    }
}