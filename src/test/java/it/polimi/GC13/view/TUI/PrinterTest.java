package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

import java.util.List;

public class PrinterTest extends TestCase {

    public void testShowDrawableCards() {

    }

    public void testShowObjectiveCard() {

    }

    public void testShowHand() {
        Printer printer = new Printer();
        printer.showHand(List.of(84));
        System.out.println("\n");

        printer.showHand(List.of(13, 15, 67));
    }
}