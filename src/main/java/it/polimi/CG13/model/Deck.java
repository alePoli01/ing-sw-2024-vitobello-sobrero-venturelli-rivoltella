package it.polimi.CG13.model;

//import of the library of Google
import java.util.*;
import com.google.gson.Gson;
import it.polimi.CG13.exception.NoOtherCards;

import java.io.FileReader;
import java.io.IOException;

public class Deck {
    LinkedList<ObjectiveCard> objectiveDeck;
    LinkedList<PlayableCard> startDeck;
    LinkedList<PlayableCard> resourceDeck;
    LinkedList<PlayableCard> goldDeck;
    private boolean emptyResourceDeck;
    private boolean emptyGoldDeck;

    // getter
    public boolean isEmptyResourceDeck() {
        return emptyResourceDeck;
    }
    // setter
    public void setEmptyResourceDeck(boolean emptyResourceDeck) {
        this.emptyResourceDeck = emptyResourceDeck;
    }
    // getter
    public boolean isEmptyGoldDeck() {
        return emptyGoldDeck;
    }
    // setter
    public void setEmptyGoldDeck(boolean emptyGoldDeck) {
        this.emptyGoldDeck = emptyGoldDeck;
    }

    public PlayableCard getNewResourceCard() throws NoOtherCards {
        if (resourceDeck.isEmpty()) {
            throw new NoOtherCards("Resource");
        } else {
            return resourceDeck.getFirst();
        }
    }

    public PlayableCard getNewGoldCard() throws NoOtherCards {
        if (goldDeck.isEmpty()) {
            throw new NoOtherCards("Gold");
        } else {
            return goldDeck.getFirst();
        }
    }
}
