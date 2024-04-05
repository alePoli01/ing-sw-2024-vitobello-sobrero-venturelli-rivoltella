package it.polimi.GC13.model;

import it.polimi.GC13.enums.Resource;
import junit.framework.TestCase;

import java.util.LinkedList;

public class ReignObjectiveTest extends TestCase {
    Deck deck =new Deck();

    public PlayableCard fetchPlayableCard(LinkedList<PlayableCard> cardList, int serialNumber){
        //fetch specified card from given deck
        for(PlayableCard cursor:cardList){
            if(serialNumber== cursor.serialNumber){
                return cursor;
            }
        }
        System.out.println("Warining: card fetch has failed!\n Returning first card in the list...");
        return cardList.getFirst();}
    public ObjectiveCard fetchObjectiveCard(LinkedList<ObjectiveCard> cardList, int serialNumber){
        //fetch specified card from given deck
        for(ObjectiveCard cursor:cardList){
            if(serialNumber== cursor.serialNumber){
                return cursor;
            }
        }
        System.out.println("Warining: card fetch has failed!\n Returning first card in the list...");
        return cardList.getFirst();}

    public void testComboCondition() {
        //objective card to test
        //ReignObjective card = new ReignObjective(1234,2,Resource.ANIMAL);
        //create board
        Player player=new Player("giocatore1") ;
        Board board=new Board(player);
        int x=0;
        int y=0;
        Coordinates xy=new Coordinates(x,y);

        deck.parseJSON();
        //add 5 Fungi resources to the board(not actually playing the card)
        for(int i=0;i<7;i++){

            board.addResource(fetchPlayableCard(deck.getResourceDeck(),i+1),true);

            xy.setY(xy.getY()+1);
            xy.setX(xy.getX()+1);
        }
        System.out.println("funghi collezionati: "+board.getCollectedResources().get(Resource.FUNGI));
        assertEquals(4,fetchObjectiveCard(deck.getObjectiveDeck(),95).getObjectivePoints(board));
    }
}