package it.polimi.CG13.model;

//import of the library of Google
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class Deck {
    private LinkedList<ObjectiveCard> objectiveDeck;
    private LinkedList<StartCard> startDeck;
    private LinkedList<PlayableCard> resourceDeck;
    private LinkedList<PlayableCard> goldDeck;

    public Deck() {
        this.objectiveDeck = new LinkedList<>();
        this.startDeck = new LinkedList<>();
        this.resourceDeck = new LinkedList<>();
        this.goldDeck = new LinkedList<>();
    }

    public LinkedList<PlayableCard> getResourceDeck() {
        return resourceDeck;
    }

    public LinkedList<PlayableCard> getGoldDeck() {
        return goldDeck;
    }

    public LinkedList<ObjectiveCard> getObjectiveDeck() {
        return objectiveDeck;
    }

    public LinkedList<StartCard> getStartDeck() {
        return startDeck;
    }

    public void shuffleDecks() {
        Collections.shuffle(objectiveDeck);
        Collections.shuffle(startDeck);
        Collections.shuffle(resourceDeck);
        Collections.shuffle(goldDeck);
    }

    public void parseJSON(){
        Gson gson = new Gson();

        //lettura carte risorsa (
        try {
            FileReader reader = new FileReader("src/main/resources/Decks.json");
            FileReader reader2 = new FileReader("src/main/resources/Starter.json");
            FileReader reader3 = new FileReader("src/main/resources/Objective.json");

            // resource / gold Deck initialization
            Type type = new TypeToken<Map<String, LinkedList<PlayableCard>>>(){}.getType();
            Map<String, LinkedList<PlayableCard>> map = gson.fromJson(reader, type);

            // start Deck initialization
            Type type2 = new TypeToken<Map<String, LinkedList<StartCard>>>(){}.getType();
            Map<String, LinkedList<StartCard>> map2 = gson.fromJson(reader2, type2);

            Type type3 = new TypeToken<Map<String, LinkedList<ObjectiveCard>>>(){}.getType();
            Map<String, LinkedList<ObjectiveCard>> map3 = gson.fromJson(reader2, type3);

            this.resourceDeck = map.get("resourceDeck");
            this.goldDeck = map.get("goldDeck");
            this.startDeck = map2.get("startDeck");
            this.objectiveDeck = map3.get("objectiveDeck");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
