package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.Position;
import junit.framework.TestCase;

public class ReaderTest extends TestCase {

    public void testUpdateMainMenu() {
        TUI t1 = new TUI();
        String nickname = "test";

        t1.nickname = nickname;
        t1.playerPositions.put(nickname, Position.FIRST);

        t1.updateTurn(nickname, true);

        t1.turnPlayed = 0;

        t1.showHomeMenu();
        t1.turnPlayed++;

        t1.updateTurn(nickname, false);

        // this calls interrupt reader
        t1.updateTurn(nickname, true);

    }
}