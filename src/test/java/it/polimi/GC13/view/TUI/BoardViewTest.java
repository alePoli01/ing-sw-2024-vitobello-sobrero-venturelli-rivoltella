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
        board.InsertCard(50,52,60,1,deck,false);

        //two card with one car between, all cases
        board.InsertCard(54,50,80,3,deck,false);
        board.InsertCard(55,53,60,1,deck,false);
        board.InsertCard(55,51,80,2,deck,false);
        board.InsertCard(54,52,43,12,deck,false);
        board.InsertCard(53,53,60,13,deck,false);
        board.InsertCard(54,54,80,0,deck,false);
        board.InsertCard(53,51,43,9,deck,false);
        //board.InsertCard(53,53,80,4,deck,false);

        board.InsertCard(58,50,43,0,deck,false);
        board.InsertCard(57,51,60,1,deck,false);
        board.InsertCard(58,52,80,2,deck,false);
        //board.InsertCard(59,53,43,3,deck,false);

        board.InsertCard(62,50,43,1,deck,false);
        board.InsertCard(61,51,60,0,deck,false);
        board.InsertCard(62,52,80,2,deck,false);

        board.InsertCard(66,50,43,0,deck,false);
        board.InsertCard(67,51,60,2,deck,false);
        board.InsertCard(66,52,80,1,deck,false);

        board.InsertCard(70,50,43,0,deck,false);
        board.InsertCard(71,51,60,1,deck,false);
        board.InsertCard(70,52,80,2,deck,false);

        board.InsertCard(74,50,43,1,deck,false);
        board.InsertCard(75,51,60,0,deck,false);
        board.InsertCard(74,52,80,2,deck,false);

        //diagoanl test
        board.InsertCard(78,50,43,0,deck,false);
        board.InsertCard(79,51,60,1,deck,false);
        board.InsertCard(80,52,80,2,deck,false);

        board.InsertCard(82,50,43,0,deck,false);
        board.InsertCard(83,51,60,2,deck,false);
        board.InsertCard(84,52,80,1,deck,false);

        board.InsertCard(86,50,43,1,deck,false);
        board.InsertCard(87,51,60,0,deck,false);
        board.InsertCard(88,52,61,2,deck,false);

        board.InsertCard(94,50,43,0,deck,false);
        board.InsertCard(93,51,60,1,deck,false);
        board.InsertCard(92,52,80,2,deck,false);

        board.InsertCard(98,50,43,0,deck,false);
        board.InsertCard(97,51,60,2,deck,false);
        board.InsertCard(96,52,80,1,deck,false);

       /* board.InsertCard(54,56,43,1,deck,false);
        board.InsertCard(53,57,60,0,deck,false);
        board.InsertCard(52,58,80,2,deck,false);*/

        board.printBoard();

    }
}