package it.polimi.CG13.model;

//import of the library of Google
import java.util.*;
import com.google.gson.Gson;
import java.io.FileReader;
import java.io.IOException;

public class Deck {
    LinkedList<ObjectiveCard> objectiveDeck;
    LinkedList<PlayableCard> startDeck;
    LinkedList<PlayableCard> resourceDeck;
    LinkedList<PlayableCard> goldDeck;
}
