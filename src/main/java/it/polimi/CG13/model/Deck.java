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
}
