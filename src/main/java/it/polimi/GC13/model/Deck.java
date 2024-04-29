package it.polimi.GC13.model;

//import of the library of Google
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.Serializable;
import java.lang.reflect.Type;
import java.io.FileReader;
import java.io.IOException;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Deck implements Serializable {
    private final LinkedList<ObjectiveCard> objectiveDeck;
    private LinkedList<StartCard> startDeck;
    private LinkedList<PlayableCard> resourceDeck;
    private LinkedList<PlayableCard> goldDeck;

    public Deck() {
        this.objectiveDeck = new LinkedList<>();
        this.startDeck = new LinkedList<>();
        this.resourceDeck = new LinkedList<>();
        this.goldDeck = new LinkedList<>();
        this.parseJSON();
        //this.shuffleDecks();
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
            FileReader readerPlayable = new FileReader("src/main/resources/Decks.json");
            FileReader readerStarter = new FileReader("src/main/resources/Starter.json");
            FileReader readerPattern = new FileReader("src/main/resources/PatternObjective.json");
            FileReader readerReign = new FileReader("src/main/resources/ReignObjective.json");
            FileReader readerObject = new FileReader("src/main/resources/ObjectObjective.json");

            LinkedList<PatternObjective> PatternDeck;
            LinkedList<ReignObjective> ReignDeck;
            LinkedList<ObjectObjective> ObjectDeck;

            // resource / gold Deck initialization
            Type playable = new TypeToken<Map<String, LinkedList<PlayableCard>>>(){}.getType();
            Map<String, LinkedList<PlayableCard>> mapPlayable = gson.fromJson(readerPlayable, playable);

            // start Deck initialization
            Type starter = new TypeToken<Map<String, LinkedList<StartCard>>>(){}.getType();
            Map<String, LinkedList<StartCard>> mapStarter = gson.fromJson(readerStarter, starter);

            Type pattern = new TypeToken<Map<String, LinkedList<PatternObjective>>>(){}.getType();
            Map<String, LinkedList<PatternObjective>> mapPattern = gson.fromJson(readerPattern, pattern);

            Type reign = new TypeToken<Map<String, LinkedList<ReignObjective>>>(){}.getType();
            Map<String, LinkedList<ReignObjective>> mapReign = gson.fromJson(readerReign, reign);

            Type object = new TypeToken<Map<String, LinkedList<ObjectObjective>>>(){}.getType();
            Map<String, LinkedList<ObjectObjective>> mapObject = gson.fromJson(readerObject, object);

            this.resourceDeck = mapPlayable.get("resourceDeck");
            this.goldDeck = mapPlayable.get("goldDeck");
            this.startDeck = mapStarter.get("startDeck");

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
            e.printStackTrace();
        }
    }
    public void cardPrinter(int serialNumber,boolean isflipped){

        //colors of the characters
        String red = "\u001b[31m";   // Red
        String green = "\u001b[32m"; // green
        String blue = "\u001b[36m";  // Blue
        String magenta = "\u001b[35m";  // Magenta
        String gold = "\u001b[93m";  // gold
        String reset = "\u001b[0m";  // reset color of the characters

        //colors of the background
        String backgroundRed = "\u001b[41m";   // red background
        String backgroundGreen = "\u001b[42m"; // Green Background
        String backgroundBlue = "\u001b[46m";  // Blue background
        String backgroundmagenta = "\u001b[35m";  // Blue
        String resetbackground = "\u001b[0m";  // Reset color of the background

        /*
Linea orizzontale: ═ (U+2500)
Linea verticale: ║ (U+2502)
Angolo in alto a sinistra: ╔ (U+250C)
Angolo in alto a destra: ╗ (U+2510)
Angolo in basso a sinistra: ╚ (U+2514)
Angolo in basso a destra: ╝ (U+2518)
Linea a T orizzontale (intersezione): ╦ (U+252C), ╩ (U+2534)
Linea a T verticale (intersezione): ╠ (U+251C), ╣ (U+2524)
Croce: ┼ (U+253C)
        */
       if(serialNumber<=40){
           //resource card
           for(PlayableCard card: this.resourceDeck){
               if(card.serialNumber==serialNumber){
                   if(isflipped){
                   System.out.println("╔═══╦═════════╦═══╗");
                   System.out.println("║"+card.edgeResource[3].toString()+"║    "+gold+card.pointsGiven+reset+"    ║"+card.edgeResource[2].toString()+"║");
                   System.out.println("╠═══╝         ╚═══╣");
                   System.out.println("╠═══╗         ╔═══╣");
                   System.out.println("║"+card.edgeResource[0].toString()+"║         ║"+card.edgeResource[1].toString()+"║");
                   System.out.println("╚═══╩═════════╩═══╝");
                    }
                    else {
                       System.out.println("╔═══╦═════════╦═══╗");
                       System.out.println("║   ║         ║   ║");
                       System.out.println("╠═══╝   " + card.reign.toString() + "   ╚═══╣");
                       System.out.println("╠═══╗         ╔═══╣");
                       System.out.println("║   ║         ║   ║");
                       System.out.println("╚═══╩═════════╩═══╝");
                    }
               }
           }
       }
       if(serialNumber>40&&serialNumber<=80){
           //gold card
           for(PlayableCard card: this.goldDeck){
               if(card.serialNumber==serialNumber){
                   if(isflipped){
                       System.out.println("╔═══╦════╦════╦═══╗");
                       System.out.println("║"+card.edgeResource[3].toString()+"║   "+gold+card.pointsGiven+reset+"║"+gold+card.condition.toString()+reset+"   ║"+card.edgeResource[2].toString()+"║");
                       System.out.println("╠═══╝         ╚═══╣");
                       System.out.println("╠═══╗         ╔═══╣");
                       System.out.println("║"+card.edgeResource[0].toString()+"║         ║"+card.edgeResource[1].toString()+"║");
                       System.out.println("╚═══╩═════════╩═══╝");
                   }
                   else {
                       System.out.println("╔═══╦═════════╦═══╗");
                       System.out.println("║   ║         ║   ║");
                       System.out.println("╠═══╝   " + card.reign.toString() + "   ╚═══╣");
                       System.out.println("╠═══╗         ╔═══╣");
                       System.out.println("║   ║         ║   ║");
                       System.out.println("╚═══╩═════════╩═══╝");
                   }
               }
           }
       }
       if(serialNumber>80&&serialNumber<=86) {
           //starter card
       }

    }
}
