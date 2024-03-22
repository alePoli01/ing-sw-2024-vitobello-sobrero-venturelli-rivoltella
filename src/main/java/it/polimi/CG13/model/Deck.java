package it.polimi.CG13.model;

//import of the library of Google
import java.util.*;

public class Deck {
    LinkedList<ObjectiveCard> objectiveDeck;
    LinkedList<PlayableCard> startDeck;
    LinkedList<PlayableCard> resourceDeck;
    LinkedList<PlayableCard> goldDeck;

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
}
