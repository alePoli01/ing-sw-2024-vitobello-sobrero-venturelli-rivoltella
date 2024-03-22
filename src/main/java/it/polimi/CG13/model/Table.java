package it.polimi.CG13.model;


import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.CardNotAddedToHand;
import it.polimi.CG13.exception.CardNotFound;
import it.polimi.CG13.exception.NoOtherCards;

import java.util.Map;

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
    public void drawCard(PlayableCard cardToDraw) throws CardNotFound, CardNotAddedToHand {
        if (cardToDraw.getCardType().equals(CardType.GOLD)) {
            if (!(cardToDraw.equals(goldFacedUp[0]) || cardToDraw.equals(goldFacedUp[1]) || cardToDraw.equals(goldFacedDown))) {
                throw new CardNotFound(cardToDraw);
            }
        } else {
            if (!(cardToDraw.equals(resourceFacedUp[0]) || cardToDraw.equals(resourceFacedUp[1]) || cardToDraw.equals(resourceFacedDown))) {
                throw new CardNotFound(cardToDraw);
            }
        }
    }

    public void getNewCard(PlayableCard cardToReplace) throws NoOtherCards {
        if (cardToReplace.getCardType().equals(CardType.GOLD)) {
            if (!deck.isEmptyGoldDeck()) {
                if (goldFacedUp[0].equals(null)) {
                    goldFacedUp[0] = deck.getNewGoldCard();
                } else if (goldFacedUp[1].equals(null)) {
                    goldFacedUp[1] = deck.getNewGoldCard();
                } else {
                    goldFacedDown = deck.getNewGoldCard();
                }
            }
        }  else {
            if (resourceFacedUp[0].equals(null)) {
                resourceFacedUp[0] = deck.getNewResourceCard();
            } else if (resourceFacedUp[1].equals(null)) {
                resourceFacedUp[1] = deck.getNewResourceCard();
            } else {
                resourceFacedDown = deck.getNewResourceCard();
            }
        }
    }
}
