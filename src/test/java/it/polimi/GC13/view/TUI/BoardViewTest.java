package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import junit.framework.TestCase;

public class BoardViewTest extends TestCase {

    public void testPrintBoard() {
        Deck deck=new Deck();
        BoardView board=new BoardView();

        //board.InsertCard(49,49,42,4,deck,true);
        board.InsertCard(51,51,42,2,deck,false);
        board.InsertCard(50,50,43,1,deck,false);
        board.InsertCard(49,49,43,0,deck,false);
        /*board.InsertCard(52,50,43,3,deck,true);
        board.InsertCard(52,52,43,4,deck,true);*/
        board.printBoard();

    }
}