package it.polimi.GC13.model;

import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import junit.framework.TestCase;

import java.awt.*;

public class PatternObjectiveTest extends TestCase {


    public void testComboCondition() throws CardNotPlacedException {

        Player player= new Player("marco");
        Board board=new Board(player);
        Deck deck=new Deck();

        Coordinates coordinates=new Coordinates(50,50);
        Coordinates coordinates1=new Coordinates(51,49);
        Coordinates coordinates2=new Coordinates(52,48);
        Coordinates coordinates3=new Coordinates(53,47);
        Coordinates coordinates4=new Coordinates(54,46);
        Coordinates coordinates5=new Coordinates(55,45);
        Coordinates coordinates6=new Coordinates(56,44);

        StartCard card = deck.getStartDeck().removeFirst();
        PlayableCard card2= deck.getResourceDeck().removeFirst();

        board.addCardToBoard(coordinates,card,true);
        board.addCardToBoard(coordinates1,card2,false);
        board.addCardToBoard(coordinates2,card2,false);
        //board.addCardToBoard(coordinates3,card2,false);
        board.addCardToBoard(coordinates4,card2,false);
        board.addCardToBoard(coordinates5,card2,false);
        board.addCardToBoard(coordinates6,card2,false);

        ObjectiveCard cardOb = deck.getObjectiveDeck().removeFirst();
        assertEquals(1,card2.serialNumber);
        assertEquals(87,cardOb.serialNumber);
        assertEquals(2,cardOb.getObjectivePoints(board));

    }
}