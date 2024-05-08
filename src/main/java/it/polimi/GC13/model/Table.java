package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.OnNewGoldCardsAvailableMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnNewResourceCardsAvailableMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table implements Serializable {
    private final Game game;
    private Map<Player, Integer> score; //vector that keeps players scores
    private final ObjectiveCard[] commonObjectiveCard;//Objective cards in common between players
    private final Map<PlayableCard, Boolean> goldCardMap =  new LinkedHashMap<>();
    private final Map<PlayableCard, Boolean> resourceCardMap = new LinkedHashMap<>();
    private final Deck deck;
    private final ArrayList<TokenColor> tokenColors;
    private final Map<Player, Board> playerBoardMap;

    //constructor of table
    public Table(Game game) {
        this.game = game;
        this.playerBoardMap = new HashMap<>();
        this.tokenColors = new ArrayList<>(Arrays.asList(TokenColor.values()));
        this.commonObjectiveCard = new ObjectiveCard[2];
        this.deck = new Deck();
        this.deck.shuffleDecks();
    }

    public Map<PlayableCard, Boolean> getGoldCardMap() {
        return goldCardMap;
    }

    public Map<PlayableCard, Boolean> getResourceCardMap() {
        return resourceCardMap;
    }

    public PlayableCard getCardFromTable(int serialNumber) throws GenericException {
        Optional<PlayableCard> cardOptional = this.resourceCardMap.keySet().stream()
                .filter(card -> card.serialNumber == serialNumber)
                .findFirst();

        if (cardOptional.isPresent()) {
            return cardOptional.get();
        } else {
            cardOptional = this.goldCardMap.keySet().stream()
                    .filter(card -> card.serialNumber == serialNumber)
                    .findFirst();
            if (cardOptional.isPresent()) {
                return cardOptional.get();
            } else {
                throw new GenericException("Card not found in any deck.");
            }
        }
    }

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
            this.resourceCardMap.put(this.deck.getResourceDeck().removeFirst(), false);
            this.goldCardMap.put(this.deck.getGoldDeck().removeFirst(), false);
        }
        this.resourceCardMap.put(this.deck.getResourceDeck().getFirst(), true);
        this.goldCardMap.put(this.deck.getGoldDeck().getFirst(), true);

        this.game.getObserver().notifyClients(new OnNewGoldCardsAvailableMessage(getCardSerialMap(this.goldCardMap)));
        this.game.getObserver().notifyClients(new OnNewResourceCardsAvailableMessage(getCardSerialMap(this.resourceCardMap)));
    }

    /*
        METHOD USED TO GET SERIAL NUMBER FROM PLAYABLE CARDS
     */
    private Map<Integer, Boolean> getCardSerialMap(Map<PlayableCard, Boolean> cardMap) {
        return cardMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().serialNumber, Map.Entry::getValue));
    }

    public int setCommonObjectiveCard(int index, ObjectiveCard objectiveCard) {
        this.commonObjectiveCard[index] = objectiveCard;
        return objectiveCard.serialNumber;
    }

    // method to pick(remove) a card from the table
    public void drawCard(PlayableCard cardToDraw) throws GenericException {
        /*
         * 1. check which type of card has been chosen
         * 2. check every possible position in which it can be
         * 3. if found: delete it; if not throw exception
         */
        if (cardToDraw.cardType.equals(CardType.GOLD)) {
            if (this.goldCardMap.containsKey(cardToDraw)) {
                if (this.goldCardMap.get(cardToDraw)) {
                    this.deck.getGoldDeck().removeFirst();
                }
                this.replaceCardInMap(this.goldCardMap, this.goldCardMap.get(cardToDraw), cardToDraw);
            } else {
                throw new GenericException("Card drawn wasn't found between the drawable gold: " + cardToDraw.serialNumber);
            }
        } else {
            // cardToDraw if of type RESOURCE
            if (this.resourceCardMap.containsKey(cardToDraw)) {
                if (this.resourceCardMap.get(cardToDraw)) {
                    this.deck.getResourceDeck().removeFirst();
                }
                this.replaceCardInMap(this.resourceCardMap, this.resourceCardMap.get(cardToDraw), cardToDraw);
            } else {
                throw new GenericException("Card drawn wasn't found between the drawable resource: " + cardToDraw.serialNumber);
            }
        }
    }

    private void replaceCardInMap(Map<PlayableCard, Boolean> cardMap, boolean isFlipped, PlayableCard cardToDraw) {
        // removes the card from the table
        cardMap.remove(cardToDraw);

        if (cardToDraw.cardType.equals(CardType.GOLD)) {
            // removes the card from the correct deck
            this.deck.getGoldDeck().removeFirst();
            if (isFlipped) {
                // gets the first card from the deck
                cardMap.put(this.deck.getGoldDeck().getFirst(), true);
            } else {
                // the old card that was covered is now visible
                cardMap.entrySet().stream()
                        .filter(Map.Entry::getValue)
                        .forEach(entry -> entry.setValue(false));

                // add the new head of the deck on the back side
                cardMap.put(this.deck.getGoldDeck().getFirst(), true);
            }
        } else {
            this.deck.getResourceDeck().removeFirst();
            if (isFlipped) {
                cardMap.put(this.deck.getResourceDeck().getFirst(), true);
            } else {
                // the old card that was covered is now visible
                cardMap.values().stream().filter(cardSide -> cardSide).findFirst().ifPresent(cardSide -> cardSide = false);
                // add the new head of the deck on the back side
                cardMap.put(this.deck.getResourceDeck().getFirst(), true);
            }
        }
    }
}
