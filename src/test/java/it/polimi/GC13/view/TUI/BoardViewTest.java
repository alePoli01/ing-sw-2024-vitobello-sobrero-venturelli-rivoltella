package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import junit.framework.TestCase;

public class BoardViewTest extends TestCase {

    public void testPrintBoard() {
        Deck deck = new Deck();
        BoardView board = new BoardView();

        /*board.InsertCard(49,49,42,4, true);
        board.InsertCard(51,51,42,1, false);
        two card with a space between
        board.InsertCard(50,50,43,0, false);
        board.InsertCard(50,52,60,1, false);

        //two card with one car between, all cases
        board.InsertCard(54,50,80,3, false);
        board.InsertCard(55,53,60,1, false);
        board.InsertCard(55,51,80,2, false);
        board.InsertCard(54,52,43,12, false);
        board.InsertCard(53,53,60,13, false);
        board.InsertCard(54,54,80,0, false);
        board.InsertCard(53,51,43,9, false);
        //board.InsertCard(53,53,80,4, false);

        board.InsertCard(58,50,43,0, false);
        board.InsertCard(57,51,60,1, false);
        board.InsertCard(58,52,80,2, false);
        //board.InsertCard(59,53,43,3, false);

        board.InsertCard(62,50,43,1, false);
        board.InsertCard(61,51,60,0, false);
        board.InsertCard(62,52,80,2, false);

        board.InsertCard(66,50,43,0, false);
        board.InsertCard(67,51,60,2, false);
        board.InsertCard(66,52,80,1, false);

        board.InsertCard(70,50,43,0, false);
        board.InsertCard(71,51,60,1, false);
        board.InsertCard(70,52,80,2, false);

        board.InsertCard(74,50,43,1, false);
        board.InsertCard(75,51,60,0, false);
        board.InsertCard(74,52,80,2, false);

        //diagoanl test
        board.InsertCard(78,50,43,0, false);
        board.InsertCard(79,51,60,1, false);
        board.InsertCard(80,52,80,2, false);

        board.InsertCard(82,50,43,0, false);
        board.InsertCard(83,51,60,2, false);
        board.InsertCard(84,52,80,1, false);

        board.InsertCard(86,50,43,1, false);
        board.InsertCard(87,51,60,0, false);
        board.InsertCard(88,52,61,2, false);

        board.InsertCard(94,50,43,0, false);
        board.InsertCard(93,51,60,1, false);
        board.InsertCard(92,52,80,2, false);

        board.InsertCard(98,50,43,0, false);
        board.InsertCard(97,51,60,2, false);
        board.InsertCard(96,52,80,1, false);
        board.InsertCard(99,51,80,6, false);

        board.InsertCard(54,56,43,1, false);
        board.InsertCard(53,57,60,0, false);
        board.InsertCard(52,58,80,2, false);
        */
        
        /*board.insertCard(50,50,83,0, true);
        board.insertCard(51,51,34,1, false);
        board.insertCard(52,52,41,2, false);
        board.insertCard(50,52,12,3, false);
        board.insertCard(53,51,45,4, false);
        board.insertCard(54,50,41,5, false);
        board.insertCard(53,49,37,6, false);
        board.insertCard(52,48,41,7, false);
        board.insertCard(51,49,71,8, false);
        board.insertCard(52,50,41,9, false);
        board.insertCard(55,53,41,9, false);
        /*for(int i=50;i<78;i++){
            board.InsertCard(i,i,57,i*2, false);
        }*/
        //board.InsertCard(53,,41,0, false);
        board.insertCard(50,50,83,0,true);
        board.insertCard(49,49,1,1,true);
        board.insertCard(50,48,17,2,true);
        board.insertCard(49,47,25,4,true);
        board.insertCard(51,51,17,5,true);
        board.insertCard(52,52,35,6,true);
        board.insertCard(53,53,35,7,true);
        board.insertCard(48,46,35,8,true);
        board.insertCard(47,45,35,9,true);
        board.insertCard(46,44,35,10,true);
        board.insertCard(45,43,35,11,true);
        board.printBoard();

    }
}