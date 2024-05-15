package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PrinterTest extends TestCase {

    public void testShowDrawableCards() {
        Printer printer = new Printer();
        Map<Integer, Boolean> goldCards = new HashMap<>();
        Map<Integer, Boolean> resourceCards = new HashMap<>();
        resourceCards.put(1, true);
        resourceCards.put(2, false);
        resourceCards.put(3, false);
        goldCards.put(60, false);
        goldCards.put(70, false);
        goldCards.put(54, true);

        System.out.println("\n--- DRAWABLE CARDS ---");
        System.out.println("--- Gold Deck ---");
        printer.showDrawableCards(goldCards);
        System.out.println("\n\n--- Resource Deck ---");
        printer.showDrawableCards(resourceCards);
    }

    public void testShowObjectiveCard() {

    }

    public void testShowHand() {
        Printer printer = new Printer();
        printer.showHand(List.of(82));
        System.out.println("\n");

        printer.showHand(List.of(13, 15, 67));
    }

    public void testWinnerString() {
        Printer printer = new Printer();
        printer.winnerString();
    }

    public void testLoserString() {
        Printer printer = new Printer();
        printer.loserString();
    }
}