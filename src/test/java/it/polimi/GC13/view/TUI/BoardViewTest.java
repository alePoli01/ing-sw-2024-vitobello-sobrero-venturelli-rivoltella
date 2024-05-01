package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import junit.framework.TestCase;

public class BoardViewTest extends TestCase {

    public void testPrintBoard() {
        Deck deck=new Deck();
        BoardView board=new BoardView();

        board.InsertCard(49,49,42,4,deck,true);
        board.InsertCard(50,50,42,0,deck,true);
        board.InsertCard(50,52,43,1,deck,true);
        board.InsertCard(51,51,43,2,deck,true);
        /*board.InsertCard(52,50,43,3,deck,true);
        board.InsertCard(52,52,43,4,deck,true);*/
        board.printBoard();

    }
}