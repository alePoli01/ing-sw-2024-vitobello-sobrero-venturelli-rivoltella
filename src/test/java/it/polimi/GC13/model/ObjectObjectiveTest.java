package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

import java.util.*;

public class ObjectObjectiveTest extends TestCase {

    public ObjectObjective fetchObjectiveCard(LinkedList<ObjectiveCard> cardList, int serialNumber){
        //fetch specified card from given deck
        for(ObjectiveCard cursor:cardList){
            if(serialNumber== cursor.serialNumber){
                return (ObjectObjective) cursor;
            }
        }
        System.out.println("Warining: card fetch has failed!\n Returning first card number 99");
        return (ObjectObjective) cardList.getFirst();
    }
    public PlayableCard fetchPlayableCard(LinkedList<PlayableCard> cardList, int serialNumber){
        //fetch specified card from given deck
        for(PlayableCard cursor:cardList){
            if(serialNumber== cursor.serialNumber){
                return cursor;
            }
        }
        System.out.println("Warining: card fetch has failed!\n Returning first card in the list...");
        return cardList.getFirst();
    }
    public void testGetObjectivePointsOnAll() {
        Deck deck = new Deck();
        deck.parseJSON();

        Player player =new Player("giocatore1");
        Board board=new Board(player);
        Game game = new Game(2,"gameName");
        try {
            game.addPlayerToGame(player);
        } catch (GenericException e) {
            fail("AddPlayerToGame exception");
        }
        int inkCode =6;
        int manuscriptCode =7;
        int quillCode =5;
        //place cards on the board
        for(int i=0;i<3;i++){
            board.addResource(fetchPlayableCard(deck.getResourceDeck(),inkCode),false);
            board.addResource(fetchPlayableCard(deck.getResourceDeck(),quillCode),false);
        }
        for(int i=0;i<2;i++){
            board.addResource(fetchPlayableCard(deck.getResourceDeck(),manuscriptCode),false);
        }
        //now we have 3 ink, 3 manuscripts and 2 quill
        System.out.println("items collected: "+Resource.QUILL+" "+board.getCollectedResources().get(Resource.QUILL));
        System.out.println("items collected: "+Resource.INKWELL+" "+board.getCollectedResources().get(Resource.INKWELL));
        System.out.println("items collected: "+Resource.MANUSCRIPT+" "+board.getCollectedResources().get(Resource.MANUSCRIPT));

        //get the objective card with all items as the objective
        assertEquals(6,fetchObjectiveCard(deck.getObjectiveDeck(),99).getObjectivePoints(board));
    }

    public void testGetObjectivePointsOnPair(){
        Deck deck = new Deck();
        deck.parseJSON();

        Player player =new Player("giocatore1");
        Board board=new Board(player);
        Game game = new Game(2,"gameName");
        try {
            game.addPlayerToGame(player);
        } catch (GenericException e) {
            fail("AddPlayerToGame exception");
        }

        int inkCode =6;
        //place cards on the board
        for(int i=0;i<5;i++){
            board.addResource(fetchPlayableCard(deck.getResourceDeck(),inkCode),false);
        }
        System.out.println("items collected: "+Resource.INKWELL+" "+board.getCollectedResources().get(Resource.INKWELL));
        //get the objective cards with pair as the objective
        //card number 101 should award points 100 and 102 shouldn't
        assertEquals(4,fetchObjectiveCard(deck.getObjectiveDeck(),101).getObjectivePoints(board));
        assertEquals(0,fetchObjectiveCard(deck.getObjectiveDeck(),100).getObjectivePoints(board));
        assertEquals(0,fetchObjectiveCard(deck.getObjectiveDeck(),102).getObjectivePoints(board));
    }
}