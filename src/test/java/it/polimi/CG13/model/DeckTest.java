package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ReignType;
import junit.framework.TestCase;

public class DeckTest extends TestCase {

    public void testParseJSON() {
        Deck deck = new Deck();

        deck.parseJSON();

        assertNotNull(deck.resourceDeck);
        assertNotNull(deck.goldDeck);
        assertNotNull(deck.startDeck);
        //assertNotNull(deck.objectiveDeck);


        for(PlayableCard card : deck.resourceDeck){
            System.out.println("Serial Number: " + card.getSerialNumber());
            System.out.println("Card Type: " + card.getCardType());
            System.out.println("Reign: " + card.getReign());
            System.out.println("Points: " + card.getPointsGiven());

            if(card.getCardType().equals(CardType.GOLD)) {
                for (ReignType reign : ReignType.values()) {
                    System.out.println("Resource Needed: "+ card.getResourceNeeded(reign));
                }
            }

            System.out.println("edge available");
            for(int i=0; i<4; i++){
                System.out.println(card.edgeAvailable(i));
            }
            System.out.println("reign edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getReignPointEdge(i));
            }
            System.out.println("object point edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getObjectPointEdge(i));
            }


            System.out.println("-----------------------");
        }

        for(PlayableCard card : deck.goldDeck){
            System.out.println("Serial Number: " + card.getSerialNumber());
            System.out.println("Card Type: " + card.getCardType());
            System.out.println("Reign: " + card.getReign());
            System.out.println("Points: " + card.getPointsGiven());

            if(card.getCardType().equals(CardType.GOLD)) {
                for (ReignType reign : ReignType.values()) {
                    System.out.println("Resource Needed: "+ card.getResourceNeeded(reign));
                }
            }

            System.out.println("edge available");
            for(int i=0; i<4; i++){
                System.out.println(card.edgeAvailable(i));
            }
            System.out.println("reign edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getReignPointEdge(i));
            }
            System.out.println("object point edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getObjectPointEdge(i));
            }


            System.out.println("-----------------------");
        }

        for(PlayableCard card : deck.startDeck){
            System.out.println("Serial Number: " + card.getSerialNumber());
            System.out.println("Card Type: " + card.getCardType());
            System.out.println("Reign: " + card.getReign());
            System.out.println("Points: " + card.getPointsGiven());

            if(card.getCardType().equals(CardType.GOLD)) {
                for (ReignType reign : ReignType.values()) {
                    System.out.println("Resource Needed: "+ card.getResourceNeeded(reign));
                }
            }

            System.out.println("edge available");
            for(int i=0; i<4; i++){
                System.out.println(card.edgeAvailable(i));
            }
            System.out.println("reign edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getReignPointEdge(i));
            }
            System.out.println("object point edge");
            for(int i=0; i<4; i++){
                System.out.println(card.getObjectPointEdge(i));
            }


            System.out.println("-----------------------");
        }

    }
}