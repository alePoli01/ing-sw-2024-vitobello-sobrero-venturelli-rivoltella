package it.polimi.GC13.view.TUI;

import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.StartCard;
import it.polimi.GC13.view.View;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;


/*
    CLASS USED TO PRINT MESSAGES FROM TUI
 */
public class Printer {
    public static final Deck visualDeck = new Deck();
    private final BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));
    int choice = 0;

    public Printer() {}

    /*
        METHOD USED TO SHOW GAME'S HISTORY
    */
    public void showHistory(List<String> gamesLog) {
        System.out.println("\n--- HISTORY ---");
        gamesLog.forEach(log -> System.out.println(log + ";"));
    }

    /*
        METHOD USED TO SHOW DRAWABLE CARDS
     */
    public void showDrawableCards(ArrayList<Integer> goldCardsAvailable, ArrayList<Integer> resourceCardsAvailable) {
        System.out.println("\n--- DRAWABLE CARDS ---");
        System.out.println("--- Gold Deck ---");
        for (PlayableCard card : visualDeck.getGoldDeck()) {
            if (goldCardsAvailable.contains(card.serialNumber)) {
                // IF FIRST CARD -> IS FLIPPED = TRUE
                card.cardPrinter(goldCardsAvailable.getFirst().equals(card.serialNumber));
            }
        }
        System.out.println("--- Resource Deck ---");
        for (PlayableCard card : visualDeck.getResourceDeck()) {
            if (resourceCardsAvailable.contains(card.serialNumber)) {
                card.cardPrinter(resourceCardsAvailable.getFirst().equals(card.serialNumber));
            }
        }
    }

    /*
        METHOD USED TO PRINT OBJECTIVE CARD
     */
    public synchronized void showObjectiveCard(String message, List<Integer> privateObjectiveCards) {
        System.out.println("\n" + message);

        visualDeck.getObjectiveDeck()
                .stream()
                .filter(card -> privateObjectiveCards.contains(card.serialNumber))
                .forEach(card -> {
                    card.printObjectiveCard();
                    System.out.println("\t    [" + card.serialNumber + "]");
                });
    }

    /*
        METHOD USED TO SHOW HOME MENU
     */
    public void comeBack(View view) {
        try {
            while (choice != 1) {
                System.out.print("\n[1] to get back to the HOME MENU: ");
                choice = Integer.parseInt(reader.readLine());
                System.out.println("SELECTION: " + choice);
            }
            view.showHomeMenu();
        } catch (IOException e) {
            System.out.print("Error: Please put a number: ");
        }
        this.choice = 0;
    }

    /*
        METHOD USED TO PRINT PLAYABLE CARDS
     */
    public void showHand(List<Integer> hand) {
        // PRINTS START CARD
        if (hand.size() == 1) {
            for (StartCard card : visualDeck.getStartDeck()) {
                for (int cardInHand : hand) {
                    if (card.serialNumber == cardInHand) {
                        card.cardPrinter(false);
                        System.out.println("       FRONT");
                        card.cardPrinter(true);
                        System.out.println("        BACK");
                    }
                }
            }
        } else {
            hand
                .forEach(serialCard -> {
                    if (serialCard >= 1 && serialCard < 41) {
                        for (PlayableCard card : visualDeck.getResourceDeck()) {
                            if (card.serialNumber == serialCard) {
                                card.cardPrinter(false);
                                System.out.println("        [" + serialCard + "]");
                            }
                        }
                    } else {
                        for (PlayableCard card : visualDeck.getGoldDeck()) {
                            if (card.serialNumber == serialCard) {
                                card.cardPrinter(false);
                                System.out.println("        [" + serialCard + "]");
                            }
                        }
                    }
                });
        }
    }
}
