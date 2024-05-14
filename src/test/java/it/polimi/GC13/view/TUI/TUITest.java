package it.polimi.GC13.view.TUI;

import junit.framework.TestCase;

public class TUITest extends TestCase {

    public void testGameOver() {
        TUI tui1 = new TUI();
        TUI tui2 = new TUI();

        tui1.gameOver("player 1");
        tui2.gameOver("player 1");
    }
}