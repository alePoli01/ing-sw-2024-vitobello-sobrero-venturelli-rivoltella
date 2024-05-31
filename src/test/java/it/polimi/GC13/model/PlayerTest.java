package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.PointsCondition;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

import java.util.List;

public class PlayerTest extends TestCase {
    Player player = new Player("player");

    //dummy variables to create a playable card
    Resource[] a = new Resource[0];

    //create a playable card
    PlayableCard card = new PlayableCard(1234, Resource.ANIMAL, CardType.STARTER, a, null, 0, PointsCondition.EDGE);
    Game game = new Game(3, "test");

    public void testAddToHand() {
        //test if card is added to hand
        try {
            player.setGame(game);
            player.addToHand(List.of(card));
            assertEquals(card, player.getHand().getFirst());
        } catch (GenericException e) {
            fail(e.getMessage());
        }
    }

    public void testRemoveFromHand() {
        //test if card is deleted from hand
        try {// first add a card to the hand then remove it
            player.setGame(game);
            player.addToHand(List.of(card));
        } catch (GenericException e) {
            fail("addToHand exception: " + e.getMessage());
        }
        try {
            //after delete there should be nothing in the hand
            player.removeFromHand(card);
            assert (player.getHand().isEmpty());
        } catch (GenericException e) {
            fail(e.getMessage());
        }

    }

    public void testSetTokenColor() {
        //test setting token color
        try {
            game.addPlayerToGame(player);
            player.setGame(game);
            player.setTokenColor(TokenColor.RED);
        } catch (GenericException e) {
            fail(e.getMessage());
        }
        Player player2 = new Player("player2");
        //test setting token color already taken

        try {
            game.addPlayerToGame(player2);
        } catch (GenericException e) {
            fail("addPlayerToGame exception: " + e.getMessage());
        }
        player2.setGame(game);
        try {
            player2.setTokenColor(TokenColor.RED);
            fail("Color RED already in game, should throw Exception");
        } catch (GenericException e) {
            assertTrue(true);
        }
    }

    public void testTurnCounter(){
        int turnCounter = player.getTurnPlayed();
        player.increaseTurnPlayed();
        int turnCounter2 = player.getTurnPlayed();
        assertEquals(0, turnCounter);
        assertEquals(1, turnCounter2);
    }

    public void testMyTurn(){
        try {
            game.addPlayerToGame(player);
        } catch (GenericException e) {
            fail("addPlayerToGame exception: " + e.getMessage());
        }
        player.setGame(game);
        assertFalse(player.isMyTurn());
        try {
            player.checkMyTurn();
            fail("Check my turn should throw GenericException");
        } catch (GenericException e) {
          System.out.println("Check my turn threw Exception as intended");
        }
        player.setMyTurn(true);
        assertTrue(player.isMyTurn());
        try {
            player.checkMyTurn();
        } catch (GenericException e) {
            fail(e.getMessage());
        }
    }
}