package it.polimi.GC13.model;

import com.google.gson.reflect.TypeToken;
import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotFoundException;
import it.polimi.GC13.exception.NoCardsLeftException;

import java.util.*;

//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table {
    private Map<String, Integer> score; //vector that keeps players scores
    private final PlayableCard[] resourceFacedUp; //resource cards faced up that can be picked
    private final PlayableCard[] goldFacedUp; //gold cards faced up that can be picked
    private PlayableCard resourceFacedDown; //resource card on the top of the deck
    private PlayableCard goldFacedDown;//gold card on the top of the deck
    private final ObjectiveCard[] commonObjectiveCard;//Objective cards in common between players
    private final Deck deck;
    private final ArrayList<TokenColor> tokenColors;
    private final Map<Player, Board> playerBoardMap;

    //constructor of table
    public Table() {
        this.playerBoardMap = new HashMap<>();
        this.tokenColors = new ArrayList<>(Arrays.asList(TokenColor.values()));
        this.resourceFacedUp = new PlayableCard[2];
        this.goldFacedUp = new PlayableCard[2];
        this.commonObjectiveCard = new ObjectiveCard[2];
        this.deck = new Deck();
        this.deck.parseJSON();
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

    public ObjectiveCard getCommonObjectiveCard(int index) {
        return commonObjectiveCard[index];
    }

    public ArrayList<TokenColor> getTokenColors() {
        return tokenColors;
    }

    public Map<Player, Board> getPlayerBoardMap() {
        return playerBoardMap;
    }

    public Deck getDeck() {
        return this.deck;
    }

    // initialize drawable card on the table
    public void tableSetup() {
        for (int i = 0; i < 2; i++) {
            this.resourceFacedUp[i] = this.deck.getResourceDeck().removeFirst();
            this.goldFacedUp[i] = this.deck.getGoldDeck().removeFirst();
        }
        this.resourceFacedDown = this.deck.getResourceDeck().removeFirst();
        this.goldFacedDown = this.deck.getGoldDeck().removeFirst();
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
                    throw new NoCardsLeftException("Gold");
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
