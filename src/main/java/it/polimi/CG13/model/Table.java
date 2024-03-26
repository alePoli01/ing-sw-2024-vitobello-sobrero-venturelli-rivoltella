package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.CardNotAddedToHandException;
import it.polimi.CG13.exception.CardNotFoundException;
import it.polimi.CG13.exception.NoCardsLeftException;

import java.util.Map;
import java.util.NoSuchElementException;

//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table {
    private Map<String, Integer> score; //vector that keeps players scores
    private PlayableCard[] resourceFacedUp; //resource cards faced up that can be picked
    private PlayableCard[] goldFacedUp; //gold cards faced up that can be picked
    private PlayableCard resourceFacedDown; //resource card on the top of the deck
    private PlayableCard goldFacedDown;//gold card on the top of the deck
    private ObjectiveCard[] commonObjectiveCard;//Objective cards in common between players
    private Deck deck;

    //constructor of table
    public Table() {
        this.resourceFacedUp = new PlayableCard[2];
        this.goldFacedUp = new PlayableCard[2];
        this.commonObjectiveCard = new ObjectiveCard[2];
    }

    /*
           DA DISCUTERE
        return score of the player of position index
        public int getScore(int index) {
            return score[index];
        }
        //set score of the player of position index
        public void setScore(int index,int score) {
            this.score[index]= score;
        }*/

    //return the resource card requested by parameter index(0 or 1)
    public PlayableCard getResourceFacedUp(int index) {
        return resourceFacedUp[index];
    }

    //set the new resource card by parameter index(0 or 1) and card(from the deck)
    public void setResourceFacedUp(int index,PlayableCard card ) {
        resourceFacedUp[index] = card;
    }

    //return the gold card faced up by parameter index(0 or 1)
    public PlayableCard getGoldFacedUp(int index) {
        return goldFacedUp[index];
    }

    //set the gold card faced up by parameter index(0 or 1) and card(from deck)
    public void setGoldFacedUp(PlayableCard card, int index) {
        goldFacedUp[index] = card;
    }

    //return the card on the top of the resource deck
    public PlayableCard getResourceFacedDown() {
        return resourceFacedDown;
    }

    //set the top card of the gold deck(after a pick)
    public void setResourceFacedDown(PlayableCard card) {
        this.resourceFacedDown = card;
    }

    //return the card on the top of the gold deck
    public PlayableCard getGoldFacedDown() {
        return goldFacedDown;
    }

    //set the card on the top of the gold deck(after a pick)
    public void setGoldFacedDown(PlayableCard goldFacedDown) {
        this.goldFacedDown = goldFacedDown;
    }

    public ObjectiveCard getCommonObjectiveCard(int index) {
        return commonObjectiveCard[index];
    }

    public void setCommonObjectiveCard(int index, ObjectiveCard objectiveCard) {
        this.commonObjectiveCard[index] = objectiveCard;
    }

    //method to pick a card from the table after
    public void drawCard(PlayableCard cardToDraw) throws CardNotFoundException, CardNotAddedToHandException {
        if (cardToDraw.cardType.equals(CardType.GOLD)) {
            if (!(cardToDraw.equals(goldFacedUp[0]) || cardToDraw.equals(goldFacedUp[1]) || cardToDraw.equals(goldFacedDown))) {
                throw new CardNotFoundException(cardToDraw);
            }
        } else {
            if (!(cardToDraw.equals(resourceFacedUp[0]) || cardToDraw.equals(resourceFacedUp[1]) || cardToDraw.equals(resourceFacedDown))) {
                throw new CardNotFoundException(cardToDraw);
            }
        }
    }

    // updates the drawn card on the table
    public void getNewCard(PlayableCard cardToReplace) throws NoCardsLeftException {
        if (cardToReplace.cardType.equals(CardType.GOLD)) {
            if (!deck.getGoldDeck().isEmpty()) {
                try {
                    if (goldFacedUp[0] == null) {
                        goldFacedUp[0] = deck.getGoldDeck().removeFirst();
                    } else if (goldFacedUp[1] == null) {
                        goldFacedUp[1] = deck.getGoldDeck().removeFirst();
                    } else {
                        goldFacedDown = deck.getGoldDeck().removeFirst();
                    }
                } catch (NoSuchElementException e) {
                    throw new NoCardsLeftException("Deck");
                }
            }
        }  else {
            if (!deck.getResourceDeck().isEmpty()) {
                try {
                    if (resourceFacedUp[0] == null) {
                        resourceFacedUp[0] = deck.getResourceDeck().removeFirst();
                    } else if (resourceFacedUp[1] == null) {
                        resourceFacedUp[1] = deck.getResourceDeck().removeFirst();
                    } else {
                        resourceFacedDown = deck.getResourceDeck().removeFirst();
                    }
                } catch (NoSuchElementException e) {
                    throw new NoCardsLeftException("Resource");
                }
            }
        }
    }
}
