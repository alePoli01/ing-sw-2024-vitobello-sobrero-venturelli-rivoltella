package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import junit.framework.TestCase;

public class BoardViewTest extends TestCase {

    public void testPrintBoard() {
        Deck deck=new Deck();
        BoardView board=new BoardView();

        board.InsertCard(50,50,42,0,deck,true);
        board.InsertCard(51,51,43,1,deck,true);
        board.printBoard();

    }
}