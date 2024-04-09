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
        //starter
        addcCardForTest(board,deck,86,50,50,true);
        //diagonal FUNGI
        addcCardForTest(board,deck,1,51,49,true);
        addcCardForTest(board,deck,1,52,48,true);
        addcCardForTest(board,deck,1,53,47,true);
        //diagonal FUNGI
        addcCardForTest(board,deck,1,54,46,true);
        addcCardForTest(board,deck,1,55,45,true);
        addcCardForTest(board,deck,1,56,44,true);
        //L pattern aniaml fungi
        addcCardForTest(board,deck,24,50,48,true);
        addcCardForTest(board,deck,24,51,47,true);
        addcCardForTest(board,deck,24,50,46,true);
        addcCardForTest(board,deck,1,51,45,true);
        //L pattern animal fungi
        addcCardForTest(board,deck,24,50,44,true);
        addcCardForTest(board,deck,24,51,43,true);
        addcCardForTest(board,deck,24,50,42,true);
        addcCardForTest(board,deck,1,51,41,true);
        //L patter fungi plant
        addcCardForTest(board,deck,1,56,46,true);
        addcCardForTest(board,deck,16,57,47,true);
        //diagonal animal
        addcCardForTest(board,deck,24,52,42,true);
        //diagonal animal
        addcCardForTest(board,deck,24,52,46,true);
        //diagonal plant
        addcCardForTest(board,deck,16,56,48,true);
        addcCardForTest(board,deck,16,57,49,true);
        addcCardForTest(board,deck,16,58,50,true);
        //L pattern plant
        addcCardForTest(board,deck,33,56,50,true);
        //diagonal insect
        addcCardForTest(board,deck,33,57,51,true);
        addcCardForTest(board,deck,33,55,49,true);
        //diagonal animal
        addcCardForTest(board,deck,24,49,51,true);
        addcCardForTest(board,deck,24,48,52,true);
        addcCardForTest(board,deck,24,47,53,true);
        //L pattern plant insect
        addcCardForTest(board,deck,16,58,48,true);
        //L pattern insect animal
        addcCardForTest(board,deck,33,49,41,true);
        addcCardForTest(board,deck,33,48,40,true);
        addcCardForTest(board,deck,33,49,39,true);
        addcCardForTest(board,deck,24,48,38,true);

        for(ObjectiveCard card : deck.getObjectiveDeck()){
            if(card instanceof PatternObjective){
                System.out.println(((PatternObjective) card).orientation+" "+((PatternObjective) card).diagonal);
                System.out.println(card.serialNumber+": "+card.getObjectivePoints(board));
            }


        }


    }

    public void addcCardForTest (Board board, Deck deck,int serialnumber,int x,int y,boolean isflipped) throws CardNotPlacedException {
        Coordinates cordinates = new Coordinates(x,y);
        for(PlayableCard card : deck.getResourceDeck()){
            if(card.serialNumber==serialnumber){
                board.addCardToBoard(cordinates,card,isflipped);
                return;
            }
        }
        for(PlayableCard card : deck.getGoldDeck()){
            if(card.serialNumber==serialnumber){
                board.addCardToBoard(cordinates,card,isflipped);
                return;
            }
        }
        for(StartCard card : deck.getStartDeck()){
            if(card.serialNumber==serialnumber){
                board.addCardToBoard(cordinates,card,isflipped);
                return;
            }
        }
    }
}