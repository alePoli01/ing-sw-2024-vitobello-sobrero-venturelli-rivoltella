package it.polimi.GC13.model;

//import of the library of Google
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.*;
import java.lang.reflect.Type;

import java.util.*;

public class Deck implements Serializable {
    private final LinkedList<ObjectiveCard> objectiveDeck = new LinkedList<>();
    private LinkedList<StartCard> startDeck = new LinkedList<>();
    private LinkedList<PlayableCard> resourceDeck = new LinkedList<>();
    private LinkedList<PlayableCard> goldDeck = new LinkedList<>();
    private final LinkedList<PlayableCard> completeDeck = new LinkedList<>();

    public Deck() {
        this.parseJSON();
        createCompleteDeck();
    }

    public LinkedList<PlayableCard> getResourceDeck() {
        return this.resourceDeck;
    }

    public LinkedList<PlayableCard> getGoldDeck() {
        return this.goldDeck;
    }

    public LinkedList<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    public LinkedList<StartCard> getStartDeck() {
        return startDeck;
    }

    private void createCompleteDeck() {
        completeDeck.addAll(this.resourceDeck);
        completeDeck.addAll(this.goldDeck);
        completeDeck.addAll(this.startDeck);
    }

    public PlayableCard getCard(int serialNumber) {
        return this.completeDeck.get(serialNumber - 1);
    }

    public void shuffleDecks() {
        Collections.shuffle(this.objectiveDeck);
        Collections.shuffle(this.startDeck);
        Collections.shuffle(this.resourceDeck);
        Collections.shuffle(this.goldDeck);
    }

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
