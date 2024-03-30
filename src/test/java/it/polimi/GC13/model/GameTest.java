package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import junit.framework.TestCase;

import java.util.*;
public class GameTest extends TestCase {

    public void testAddPlayer() {
    }
    public void testParseJSON(){
    }
    public void testGiveStartCard() {

        Player player1=new Player("giocatore1");
        Board board=new Board(player1);
        Game game = new Game(player1,1);
        try {
            game.giveStartCard();
            assertNotNull(player1.getHand().getFirst());
        }
        catch (CardNotAddedToHandException e){
            fail("card not added to hand");
        }
        }
}