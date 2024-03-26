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
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);


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

        for(PlayableCard card : deck.goldDeck){
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

        for(PlayableCard card : deck.startDeck){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);



            System.out.println("edge available");
            for(int i=0; i<4; i++){
                System.out.println(card.linkableEdge[i]);
            }
            System.out.println("reign edge");
            for(int i=0; i<4; i++){
                System.out.println(card.reignPointEdge[i]);
            }
            System.out.println("object point edge");
            for(int i=0; i<4; i++){
                System.out.println(card.objectPointEdge[i]);
            }


            System.out.println("-----------------------");
        }

    }
}