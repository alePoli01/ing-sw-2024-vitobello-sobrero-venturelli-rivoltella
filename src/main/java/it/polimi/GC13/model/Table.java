package it.polimi.GC13.model;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.OnNewGoldCardsAvailableMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnNewResourceCardsAvailableMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerScoreUpdateMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table implements Serializable {
    private final Game game;
    private final Map<Player, Integer> playersScore = new HashMap<>(); //vector that keeps players scores
    private final List<ObjectiveCard> commonObjectiveCard = new LinkedList<>();//Objective cards in common between players
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
        try{
            return this.goldCardMap.keySet().stream()
                        .filter(card -> card.serialNumber == serialNumber)
                        .findFirst()
                        .orElseThrow();
        } catch (NoSuchElementException e){
            try {
                return this.resourceCardMap.keySet()
                        .stream()
                        .filter(card -> card.serialNumber == serialNumber)
                        .findFirst()
                        .orElseThrow();
            } catch (NoSuchElementException ex) {
                throw new GenericException("Card not found in any deck.");
            }
        }
    }

    public ArrayList<TokenColor> getTokenColors() {
        return tokenColors;
    }

    public List<ObjectiveCard> getCommonObjectiveCard() {
        return this.commonObjectiveCard;
    }

    public Map<Player, Board> getPlayerBoardMap() {
        return playerBoardMap;
    }

    public Map<Player, Integer> getPlayersScore() {
        return this.playersScore;
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

    /*
        METHOD THAT IDENTIFIES THE TYPE OF THE CARD
        THEN CALLS THE METHOD TO UPDATE CARD/RESOURCE CARD MAP AND THE DECK
     */
    public void drawCard(PlayableCard cardToDraw) throws GenericException {
        // if cardToDraw is the goldMap
        if (this.goldCardMap.containsKey(cardToDraw)) {
            this.updateDrawableCards(this.goldCardMap, cardToDraw, this.deck.getGoldDeck(), this.deck.getResourceDeck());
            // update clients
            this.game.getObserver().notifyClients(new OnNewGoldCardsAvailableMessage(getCardSerialMap(this.goldCardMap)));
        } else if (this.resourceCardMap.containsKey(cardToDraw)) {
            this.updateDrawableCards(this.resourceCardMap, cardToDraw, this.deck.getResourceDeck(), this.deck.getGoldDeck());
            // update clients
            this.game.getObserver().notifyClients(new OnNewResourceCardsAvailableMessage(getCardSerialMap(this.resourceCardMap)));
        } else {
            throw new GenericException("Card: " + cardToDraw.serialNumber + " not found in any deck.");
        }
        System.out.println("GOLD");
        this.goldCardMap.forEach((card, side) -> System.out.println(card.serialNumber + " " + side + "; "));
        System.out.println("RESOURCE");
        this.resourceCardMap.forEach((card, side) -> System.out.println(card.serialNumber + " " + side + "; "));
    }

    /*
        METHOD CALLED FROM DRAW CARD
     */
    private void updateDrawableCards(Map<PlayableCard, Boolean> deckCardMap, PlayableCard cardToDraw, LinkedList<PlayableCard> deckToManage, LinkedList<PlayableCard> backupDeck) throws GenericException {
        // if the card is covered
        if (deckCardMap.get(cardToDraw)) {
            // it removes the card from the deck
            deckToManage.removeFirst();
        } else {
            // the old card that was covered is now visible
            deckCardMap.entrySet()
                    .stream()
                    .filter(Map.Entry::getValue)
                    .forEach(entry -> entry.setValue(false));
        }
        // it removes the card from the drawable
        deckCardMap.remove(cardToDraw);
        // it adds the card from the correct deck
        if (!deckCardMap.isEmpty()) {
            deckCardMap.put(deckToManage.getFirst(), true);
        } else if (backupDeck.get(1) != null) {
            System.out.println("Card drawn from the backup deck");
            deckCardMap.put(backupDeck.get(1), true);
        } else {
            throw new GenericException("Both decks are empty");
        }
    }

    public void setPlayerScore(Player player, int newPlayerScore) {
        System.out.println("Dovrei aggiornare " + player.getNickname() + " score to: " + newPlayerScore);
        this.playersScore.put(player, newPlayerScore);

        this.game.getObserver().notifyClients(new OnPlayerScoreUpdateMessage(player.getNickname(), newPlayerScore));
        System.out.println(player.getNickname() + " score updated to " + newPlayerScore);
    }
}
