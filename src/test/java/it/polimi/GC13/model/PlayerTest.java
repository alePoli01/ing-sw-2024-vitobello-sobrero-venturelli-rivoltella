package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.PointsCondition;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardStillOnHandException;
import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class PlayerTest extends TestCase {
    Player player=new Player("giocatore1");

    //dummy variables to create a playable card
    Resource []a = new Resource[0];
    Map<Resource, Integer> resourceNeeded = null;

    //create a playable card
    PlayableCard card=new PlayableCard(1234, Resource.ANIMAL, CardType.STARTER, a, resourceNeeded, 0, PointsCondition.EDGE);

    public void testAddToHand(){
        //test if card is added to hand
        try{
            player.addToHand(card);
            assertEquals(card,player.getHand().getFirst());
        }
        catch(CardNotAddedToHandException e){
            fail(e.getMessage());
        }
    }
    public void testHandUpdate(){
        //test if card is deleted from hand
        try{// first add a card to the hand then remove it
            player.addToHand(card);
        }
        catch(CardNotAddedToHandException e){
            fail("addToHand exception: "+e.getMessage());
        }
        try{
            //after delete there should be nothing in the hand
            player.handUpdate(card);
            assertEquals(true,player.getHand().isEmpty());
        }
        catch(CardStillOnHandException e){
            fail(e.getMessage());
        }

    }
}