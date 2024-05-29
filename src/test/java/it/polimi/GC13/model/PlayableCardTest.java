package it.polimi.GC13.model;

import junit.framework.TestCase;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class PlayableCardTest extends TestCase {

    public void testLinePrinter() {

        //fetching cards
        Deck deck = new Deck();
        List<Integer> serialNumbers = new ArrayList<>();
        serialNumbers.add(1);
        serialNumbers.add(13);
        serialNumbers.add(25);
        serialNumbers.add(34);
        serialNumbers.add(41);
        serialNumbers.add(51);
        serialNumbers.add(65);
        serialNumbers.add(78);
        Optional<PlayableCard> card;
        try {
            for (int k=0; k<serialNumbers.size(); k++) {
                if (serialNumbers.get(k) < 41) {
                    int finalK1 = k;
                    card = deck.getResourceDeck().stream()
                            .filter(cards -> cards.serialNumber == serialNumbers.get(finalK1)).findFirst();
                    //print card upright
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (card.isPresent()) {
                                card.get().linePrinter(i, j, true);
                                System.out.print("\n");
                            }
                        }
                    }
                    //print card flipped
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (card.isPresent()) {
                                card.get().linePrinter(i, j, false);
                                System.out.print("\n");
                            }
                        }
                    }
                } else {
                    int finalK = k;
                    card = deck.getGoldDeck().stream()
                            .filter(cards -> cards.serialNumber == serialNumbers.get(finalK)).findFirst();
                    //print card upright
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (card.isPresent()) {
                                card.get().linePrinter(i, j, true);
                                System.out.print("\n");
                            }
                        }
                    }
                    //print card flipped
                    for (int i = 0; i < 9; i++) {
                        for (int j = 0; j < 8; j++) {
                            if (card.isPresent()) {
                                card.get().linePrinter(i, j, false);
                                System.out.print("\n");
                            }
                        }
                    }

                }

            }
        }catch (Exception e) {
            fail(e.getMessage());
        }
        assertTrue(true);
    }
}