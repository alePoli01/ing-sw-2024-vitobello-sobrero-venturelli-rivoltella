package it.polimi.GC13.model;

//import of the library of Google
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.util.*;

/**
 * {@code Deck} class represents a collection of different card decks used in the game.
 * It includes objective, starter, resource, and gold card decks.
 */
public class Deck implements Serializable {
    private final LinkedList<ObjectiveCard> objectiveDeck = new LinkedList<>();
    private LinkedList<StartCard> startDeck = new LinkedList<>();
    private LinkedList<PlayableCard> resourceDeck = new LinkedList<>();
    private LinkedList<PlayableCard> goldDeck = new LinkedList<>();
    private final LinkedList<PlayableCard> completeDeck = new LinkedList<>();

    /**
     * Constructs a new {@code Deck} and initializes it by parsing card data from JSON files.
     */
    public Deck() {
        this.parseJSON();
        createCompleteDeck();
    }

    /**
     * Retrieves the resource deck.
     *
     * @return The resource deck.
     */
    public LinkedList<PlayableCard> getResourceDeck() {
        return this.resourceDeck;
    }

    /**
     * Retrieves the gold deck.
     *
     * @return The gold deck.
     */
    public LinkedList<PlayableCard> getGoldDeck() {
        return this.goldDeck;
    }

    /**
     * Retrieves the objective deck containing objective cards.
     *
     * @return The objective deck.
     */
    public LinkedList<ObjectiveCard> getObjectiveDeck() {
        return this.objectiveDeck;
    }


    /**
     * Retrieves a specific objective card from the objective deck based on its serial number.
     *
     * @param index The serial number of the objective card to retrieve.
     * @return The objective card with the specified serial number.
     */
    public ObjectiveCard getObjectiveCard(int index) {
        return this.objectiveDeck.stream().filter(card -> card.serialNumber == index).findFirst().orElseThrow();
    }

    /**
     * Retrieves the start deck containing starter cards.
     *
     * @return The start deck.
     */
    public LinkedList<StartCard> getStartDeck() {
        return this.startDeck;
    }


    /**
     * Creates a complete deck by combining resource, gold, and start decks.
     */
    private void createCompleteDeck() {
        completeDeck.addAll(this.resourceDeck);
        completeDeck.addAll(this.goldDeck);
        completeDeck.addAll(this.startDeck);
    }


    /**
     * Retrieves a playable card from the complete deck based on its serial number.
     *
     * @param serialNumber The serial number of the card to retrieve.
     * @return The playable card with the specified serial number.
     */
    public PlayableCard getCard(int serialNumber) {
        return this.completeDeck.get(serialNumber - 1);
    }

    /**
     * Shuffles all decks (objective, start, resource, and gold decks).
     */
    public void shuffleDecks() {
        Collections.shuffle(this.objectiveDeck);
        Collections.shuffle(this.startDeck);
        Collections.shuffle(this.resourceDeck);
        Collections.shuffle(this.goldDeck);
    }

    /**
     * Parses card data from JSON files and initializes the respective decks.
     * Uses Gson library for JSON parsing.
     */
    public void parseJSON() {
        Gson gson = new Gson();

        try {
            BufferedReader readerPlayable = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("Decks.json"))));
            BufferedReader readerStarter = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("Starter.json"))));
            BufferedReader readerPattern = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("PatternObjective.json"))));
            BufferedReader readerReign = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("ReignObjective.json"))));
            BufferedReader readerObject = new BufferedReader(new InputStreamReader(Objects.requireNonNull(Deck.class.getResourceAsStream("ObjectObjective.json"))));

            LinkedList<PatternObjective> PatternDeck;
            LinkedList<ReignObjective> ReignDeck;
            LinkedList<ObjectObjective> ObjectDeck;

            // resource / gold Deck initialization
            Type playable = new TypeToken<Map<String, LinkedList<PlayableCard>>>(){}.getType();
            Map<String, LinkedList<PlayableCard>> mapPlayable = gson.fromJson(readerPlayable, playable);
            this.resourceDeck = mapPlayable.get("resourceDeck");
            this.goldDeck = mapPlayable.get("goldDeck");

            // start Deck initialization
            Type starter = new TypeToken<Map<String, LinkedList<StartCard>>>(){}.getType();
            Map<String, LinkedList<StartCard>> mapStarter = gson.fromJson(readerStarter, starter);
            this.startDeck = mapStarter.get("startDeck");

            Type pattern = new TypeToken<Map<String, LinkedList<PatternObjective>>>(){}.getType();
            Map<String, LinkedList<PatternObjective>> mapPattern = gson.fromJson(readerPattern, pattern);

            Type reign = new TypeToken<Map<String, LinkedList<ReignObjective>>>(){}.getType();
            Map<String, LinkedList<ReignObjective>> mapReign = gson.fromJson(readerReign, reign);

            Type object = new TypeToken<Map<String, LinkedList<ObjectObjective>>>(){}.getType();
            Map<String, LinkedList<ObjectObjective>> mapObject = gson.fromJson(readerObject, object);

            PatternDeck = mapPattern.get("PatternDeck");
            ReignDeck = mapReign.get("ReignDeck");
            ObjectDeck = mapObject.get("ObjectDeck");
            this.objectiveDeck.addAll(PatternDeck);
            this.objectiveDeck.addAll(ReignDeck);
            this.objectiveDeck.addAll(ObjectDeck);

            readerPlayable.close();
            readerStarter.close();
            readerPattern.close();
            readerReign.close();
            readerObject.close();

        } catch (IOException e) {
            System.err.println("Error parsing file from Json.");
        }
    }
}
