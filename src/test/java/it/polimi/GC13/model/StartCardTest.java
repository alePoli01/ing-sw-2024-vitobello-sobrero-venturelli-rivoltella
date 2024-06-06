package it.polimi.GC13.model;

import junit.framework.TestCase;

public class StartCardTest extends TestCase {

    public void testLinePrinter() {
        //fetching cards
        Deck deck = new Deck();
        try {
            PlayableCard card = deck.getStartDeck().getFirst();
            //print card upright
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 8; j++) {
                    card.linePrinter(i, j, true);
                    System.out.print("\n");
                }
            }
            //print card flipped
            for (int i = 0; i < 9; i++) {
                for (int j = 0; j < 8; j++) {
                    card.linePrinter(i, j, false);
                    System.out.print("\n");
                }
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

}