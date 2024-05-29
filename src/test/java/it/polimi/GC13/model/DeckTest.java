package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.Resource;
import junit.framework.TestCase;

public class DeckTest extends TestCase {

    public void testParseJSON() {
        Deck deck = new Deck();

        deck.parseJSON();

        assertNotNull(deck.getResourceDeck());
        assertNotNull(deck.getGoldDeck());
        assertNotNull(deck.getStartDeck());

        for(PlayableCard card : deck.getResourceDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);
            System.out.println("Condition: " + card.condition);


            System.out.println("-RESOURCE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.edgeResource[i]);
            }

            System.out.println("-----------------------");
        }

        for(PlayableCard card : deck.getGoldDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);

            if(card.cardType.equals(CardType.GOLD)) {
                for (Resource reign : Resource.values()) {
                    if (reign.isReign()) {
                        System.out.println("Resource Needed of " + reign + ": " + card.resourceNeeded.get(reign));
                    }
                }
            }

            System.out.println("-RESOURCE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.edgeResource[i]);
            }

            System.out.println("-----------------------");
        }

        for(StartCard card : deck.getStartDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Card Type: " + card.cardType);
            System.out.println("Reign: " + card.reign);
            System.out.println("Points: " + card.pointsGiven);



            System.out.println("-RESOURCE AVAILABLE-");
            for(int i=0; i<4; i++){
                System.out.println(i+": "+card.edgeResource[i]);
            }

            System.out.println("-BACK REIGN-");
            for(int i = 0; i<card.backReigns.length; i++) {
                System.out.println(card.backReigns[i]);
            }

            System.out.println("-----------------------");
        }

        //test of ObjectiveCard
        for(ObjectiveCard card:deck.getObjectiveDeck()){
            System.out.println("Serial Number: " + card.serialNumber);
            System.out.println("Combo Points: " + card.comboPoints);

            if(card instanceof PatternObjective){
                System.out.println("Diagonal: " + ((PatternObjective) card).diagonal);
                System.out.println("Orientation: " + ((PatternObjective) card).orientation);
            }
            if(card instanceof ReignObjective){
                System.out.println("Type: " + ((ReignObjective) card).type);
            }
            if(card instanceof ObjectObjective){
                for(int i=0;i<((ObjectObjective) card).object.size();i++){
                    System.out.println("Object required: " + ((ObjectObjective) card).object.get(i));
                }
            }

            System.out.println("-----------------------");
        }

    }

    public void testPrintObjectiveCard() {
        Deck deck = new Deck();
        for(ObjectiveCard card: deck.getObjectiveDeck()){
            card.printObjectiveCard();
        }
    }
}