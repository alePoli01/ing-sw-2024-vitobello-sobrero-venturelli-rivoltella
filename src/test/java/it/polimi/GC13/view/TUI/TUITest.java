package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

import java.util.HashSet;
import java.util.Set;

public class TUITest extends TestCase {

    public void testGameOver() {
        TUI tui1 = new TUI();

        Set<String> winners = new HashSet<>();
        winners.add("player 1");

        tui1.gameOver(winners);

        TUI tui2 = new TUI();
        tui2.gameOver(winners);
    }
}