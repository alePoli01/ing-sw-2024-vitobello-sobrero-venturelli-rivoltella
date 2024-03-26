package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.ReignType;
import junit.framework.TestCase;

public class DeckTest extends TestCase {

    public void testParseJSON() {
        Deck deck = new Deck();

        deck.parseJSON();

        assertNotNull(deck.getResourceDeck());
        assertNotNull(deck.getGoldDeck());
        assertNotNull(deck.getStartDeck());
        //assertNotNull(deck.objectiveDeck);


        for(PlayableCard card : deck.getResourceDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);
            System.out.println("Condition: " + card.condition);


            System.out.println("-EDGE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.linkableEdge[i]);
            }
            System.out.println("-REIGN EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.reignPointEdge[i]);
            }
            System.out.println("-OBJECT EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": " + card.objectPointEdge[i]);
            }


            System.out.println("-----------------------");
        }

        for(PlayableCard card : deck.getGoldDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);

            if(card.cardType.equals(CardType.GOLD)) {
                for (ReignType reign : ReignType.values()) {
                    System.out.println("Resource Needed of "+reign.toString()+": "+ card.resourceNeeded.get(reign));
                }
            }

            System.out.println("-EDGE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.linkableEdge[i]);
            }
            System.out.println("-REIGN EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.reignPointEdge[i]);
            }
            System.out.println("-OBJECT EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.objectPointEdge[i]);
            }


            System.out.println("-----------------------");
        }

        for(StartCard card : deck.getStartDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);



            System.out.println("-EDGE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(card.linkableEdge[i]);
            }
            System.out.println("-REIGN EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(card.reignPointEdge[i]);
            }
            System.out.println("-OBJECT EDGE-");
            for(int i=0; i<4; i++){
                System.out.println(card.objectPointEdge[i]);
            }

            System.out.println("-FRONT REIGN-");
            for(int i = 0; i<card.frontReigns.size(); i++) {
                System.out.println(card.frontReigns.get(i));
            }

            System.out.println("-----------------------");
        }

    }
}