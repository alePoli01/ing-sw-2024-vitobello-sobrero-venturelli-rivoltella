package it.polimi.GC13.model;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.messages.fromserver.OnNewGoldCardsAvailableMessage;
import it.polimi.GC13.network.messages.fromserver.OnNewResourceCardsAvailableMessage;
import it.polimi.GC13.network.messages.fromserver.OnPlayerScoreUpdateMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

/**
 * {@code Table} class represents the game table, storing available cards and player scores.
 */

public class Table implements Serializable {

    /**
     * Reference to the {@link Game} object associated with the table.
     */
    private final Game game;

    /**
     * Map that stores the scores of each player.
     */
    private final Map<Player, Integer> playersScore = new HashMap<>(); //vector that keeps players scores

    /**
     * List of {@link ObjectiveCard} objects that are common between players.
     */
    private final List<ObjectiveCard> commonObjectiveCard = new LinkedList<>();//Objective cards in common between players

    /**
     * LinkedHashMap mapping {@link PlayableCard} objects to a boolean, indicating the side of the card shown.
     * <p>
     * The boolean value {@code false} indicates the front side of the card, while {@code true} indicates the back side.
     */
    private final Map<PlayableCard, Boolean> goldCardMap =  new LinkedHashMap<>();

    /**
     * LinkedHashMap mapping {@link PlayableCard} objects to a boolean, indicating the side of the card shown.
     * <p>
     * The boolean value {@code false} indicates the front side of the card, while {@code true} indicates the back side.
     */
    private final Map<PlayableCard, Boolean> resourceCardMap = new LinkedHashMap<>();

    /**
     * Deck of cards used in the game, represented by {@link Deck}.
     */
    private final Deck deck;

    /**
     * ArrayList containing all available {@link TokenColor} values, representing the available token colors.
     */
    private final ArrayList<TokenColor> tokenColors = new ArrayList<>(Arrays.asList(TokenColor.values()));

    /**
     * Map that associates each {@link Player} with their corresponding {@link Board} object.
     */
    private final Map<Player, Board> playerBoardMap = new HashMap<>();



    /**
     * Constructs a {@code Table} with a specified game.
     *
     * @param game the game referred to the game
     */
    //constructor of table
    public Table(Game game) {
        this.game = game;
        this.deck = new Deck();
        this.deck.shuffleDecks();
    }
    /**
     * Gets the map of gold cards on the table.
     *
     * @return the map of gold cards.
     */
    public Map<PlayableCard, Boolean> getGoldCardMap() {
        return goldCardMap;
    }

    /**
     * Gets the map of resource cards on the table.
     *
     * @return the map of resource cards.
     */
    public Map<PlayableCard, Boolean> getResourceCardMap() {
        return resourceCardMap;
    }

    /**
     * Gets a card from the table by its serial number.
     *
     * @param serialNumber the serial number of the card to get.
     * @return the playable card with the specified serial number.
     * @throws GenericException if the card is not found.
     */
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

    /**
     * Gets the list of token colors available on the table.
     *
     * @return the list of token colors.
     */
    public ArrayList<TokenColor> getTokenColors() {
        return tokenColors;
    }

    /**
     * Gets the list of common objective cards.
     *
     * @return the list of common objective cards.
     */
    public List<ObjectiveCard> getCommonObjectiveCard() {
        return this.commonObjectiveCard;
    }

    /**
     * Gets the map of player boards.
     *
     * @return the map of player boards.
     */
    public Map<Player, Board> getPlayerBoardMap() {
        return playerBoardMap;
    }

    /**
     * Gets the map of players' scores.
     *
     * @return the map of players' scores.
     */
    public Map<Player, Integer> getPlayersScore() {
        return this.playersScore;
    }

    /**
     * Gets the deck of cards for the game.
     *
     * @return the deck of cards.
     */
    public Deck getDeck() {
        return this.deck;
    }

    /**
     * Initializes the drawable cards on the table.
     */
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


    /**
     * Gets the serial numbers from the playable cards in the given map.
     *
     * @param cardMap the card map to get serial numbers from.
     * @return a map of serial numbers and their visibility status.
     */
    public Map<Integer, Boolean> getCardSerialMap(Map<PlayableCard, Boolean> cardMap) {
        return cardMap.entrySet()
                .stream()
                .collect(Collectors.toMap(e -> e.getKey().serialNumber, Map.Entry::getValue));
    }

    /**
     * Draws a card from the table.
     *
     * @param cardToDraw the card to draw.
     * @throws GenericException if the card is not found in any deck.
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
    }



    /**
     * method used to update deck card map after a card has been drawn
     * Updates the drawable cards on the table.
     *
     * @param deckCardMap the map of cards to update.
     *  * @param drawnCard the card that was drawn.
     *  * @param deckToManage the deck to manage.
     *  * @param backupDeck the backup deck to use if the main deck is empty.
     *  * @throws GenericException if both decks are empty.
     */
    private void updateDrawableCards(Map<PlayableCard, Boolean> deckCardMap, PlayableCard drawnCard, LinkedList<PlayableCard> deckToManage, LinkedList<PlayableCard> backupDeck) throws GenericException {
        // if the card isn't covered
        if (!deckCardMap.get(drawnCard)) {
            // the old card that was covered is now visible
            deckCardMap.entrySet()
                    .stream()
                    .filter(Map.Entry::getValue)
                    .findFirst()
                    .ifPresent(entry -> entry.setValue(false));
        }
        // it removes the card the first card from the deck because it was still available in case the other deck finished
        deckToManage.removeFirst();
        // it removes the card from the drawable cards
        deckCardMap.remove(drawnCard);
        // it adds the card from the correct deck
        if (!deckToManage.isEmpty()) {
            deckCardMap.put(deckToManage.getFirst(), true);
        } else if (backupDeck.size() >= 2) {
            System.out.println("Card drawn from the backup deck");
            deckCardMap.put(backupDeck.get(1), true);
        } else {
            throw new GenericException("Both decks are empty");
        }
    }

    /**
     * Adds new points to a player.
     *
     * @param player the player that scored new points.
     * @param newPoints the amount of points to add.
     */
    public void addPlayerScore(Player player, int newPoints) {
        if (!this.playersScore.containsKey(player)) {
            this.playersScore.put(player, newPoints);
        } else {
            this.playersScore.computeIfPresent(player, (k, v) -> v + newPoints);
        }

        this.game.getObserver().notifyClients(new OnPlayerScoreUpdateMessage(player.getNickname(), this.playersScore.get(player)));
        System.out.println(player.getNickname() + " score updated to " + this.playersScore.get(player));
    }
}
