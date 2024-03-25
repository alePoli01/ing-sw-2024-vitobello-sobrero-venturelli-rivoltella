package it.polimi.CG13.model;

//import of the library of Google
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;

public class Deck {
    LinkedList<ObjectiveCard> objectiveDeck;
    LinkedList<PlayableCard> startDeck;
    LinkedList<PlayableCard> resourceDeck;
    LinkedList<PlayableCard> goldDeck;

    public Deck() {
        this.objectiveDeck = null;
        this.startDeck = null;
        this.resourceDeck = null;
        this.goldDeck = null;
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

    public LinkedList<PlayableCard> getStartDeck() {
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

            // Definisci il tipo per deserializzare
            Type type = new TypeToken<Map<String, LinkedList<PlayableCard>>>(){}.getType();
            Map<String, LinkedList<PlayableCard>> map = gson.fromJson(reader, type);

            this.resourceDeck = map.get("resourceDeck");
            this.goldDeck = map.get("goldDeck");
            this.startDeck = map.get("startDeck");

            reader.close();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


}


