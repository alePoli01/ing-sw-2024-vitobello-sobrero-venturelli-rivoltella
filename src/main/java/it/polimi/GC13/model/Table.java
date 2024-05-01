package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotFoundException;
import it.polimi.GC13.exception.NoCardsLeftException;

import java.io.Serializable;
import java.util.*;

//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table implements Serializable {
    private Map<Player, Integer> score; //vector that keeps players scores
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
        this.tokenColors.forEach(System.out::println);
        this.resourceFacedUp = new PlayableCard[2];
        this.goldFacedUp = new PlayableCard[2];
        this.commonObjectiveCard = new ObjectiveCard[2];
        this.deck = new Deck();
        this.deck.parseJSON();
    }

    /*
    TODO: tableSetup potrebbe essere sostituito con getNewCard(chiamato più volte)?
    DA DISCUTERE:
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

    public PlayableCard[] getResourceFacedUp() {
        return resourceFacedUp;
    }

    public PlayableCard[] getGoldFacedUp() {
        return goldFacedUp;
    }

    public PlayableCard getResourceFacedDown() {
        return resourceFacedDown;
    }

    public PlayableCard getGoldFacedDown() {
        return goldFacedDown;
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

    public int setCommonObjectiveCard(int index, ObjectiveCard objectiveCard) {
        this.commonObjectiveCard[index] = objectiveCard;
        return objectiveCard.serialNumber;
    }

    //method to pick(remove) a card from the table
    public void drawCard(PlayableCard cardToDraw) throws CardNotFoundException, CardNotAddedToHandException {
        /**
         * 1. check which type of card has been chosen
         * 2. check every possible position in which it can be
         * 3. if found: delete it; if not throw exception
         */
        if (cardToDraw.cardType.equals(CardType.GOLD)) {
            if (cardToDraw.equals(goldFacedUp[0])) {
                goldFacedUp[0] = null;
            } else if (cardToDraw.equals(goldFacedUp[1])) {
                goldFacedUp[1] = null;
            } else if (cardToDraw.equals(goldFacedDown)) {
                goldFacedDown = null;
            } else {
                throw new CardNotFoundException(cardToDraw);
            }
        } else {
            // cardToDraw if of type RESOURCE
            if (cardToDraw.cardType.equals(CardType.RESOURCE)) {
                if (cardToDraw.equals(resourceFacedUp[0])) {
                    resourceFacedUp[0] = null;
                } else if (cardToDraw.equals(resourceFacedUp[1])) {
                    resourceFacedUp[1] = null;
                } else if (cardToDraw.equals(resourceFacedDown)) {
                    resourceFacedDown = null;
                } else {
                    throw new CardNotFoundException(cardToDraw);
                }
            }
        }
    }

    // updates the drawn card on the table
    public void getNewCard(PlayableCard cardToReplace) throws NoCardsLeftException {
        /**
         * 1. check if both decks are empty, if true there's nothing to be done (else branch)
         * 2. check which type of card needs to be replaced
         * 3. use the first deck available( for gold check goldDeck first,similar for resource)
         *
         * the method doesn't check if the card to replace is already on the table, the controller will make sure it isn't
         */
        if (!this.deck.getGoldDeck().isEmpty() || !this.deck.getResourceDeck().isEmpty()) {
            //one or both decks are NOT empty
            if (cardToReplace.cardType.equals(CardType.GOLD)) {
                if (!deck.getGoldDeck().isEmpty()) {
                    //the gold card is drawn from the gold deck
                    if (goldFacedUp[0] == null) {
                        goldFacedUp[0] = deck.getGoldDeck().removeFirst();
                    } else if (goldFacedUp[1] == null) {
                        goldFacedUp[1] = deck.getGoldDeck().removeFirst();
                    } else {
                        goldFacedDown = deck.getGoldDeck().removeFirst();
                    }
                } else {
                    //the gold card is drawn from the resource deck
                    if (goldFacedUp[0] == null) {
                        goldFacedUp[0] = deck.getResourceDeck().removeFirst();
                    } else if (goldFacedUp[1] == null) {
                        goldFacedUp[1] = deck.getResourceDeck().removeFirst();
                    } else {
                        goldFacedDown = deck.getResourceDeck().removeFirst();
                    }
                }
            } else {
                //cardToReplace is RESOURCE
                if (!deck.getResourceDeck().isEmpty()) {
                    //the resource card is drawn from the resource deck
                    if (resourceFacedUp[0] == null) {
                        resourceFacedUp[0] = deck.getResourceDeck().removeFirst();
                    } else if (resourceFacedUp[1] == null) {
                        resourceFacedUp[1] = deck.getResourceDeck().removeFirst();
                    } else {
                        resourceFacedDown = deck.getResourceDeck().removeFirst();
                    }
                } else {
                    //the resource card is drawn from the gold deck
                    if (resourceFacedUp[0] == null) {
                        resourceFacedUp[0] = deck.getGoldDeck().removeFirst();
                    } else if (resourceFacedUp[1] == null) {
                        resourceFacedUp[1] = deck.getGoldDeck().removeFirst();
                    } else {
                        resourceFacedDown = deck.getGoldDeck().removeFirst();
                    }
                }
            }
        } else {
            throw new NoCardsLeftException("Every ");

        }
    }

}
