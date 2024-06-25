package it.polimi.GC13.view.TUI;

import it.polimi.GC13.enums.Position;
import junit.framework.TestCase;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

public class TUITest extends TestCase {

    public void testGameOver() {
        TUI tui1 = new TUI();

        Set<String> winners = new HashSet<>();
        winners.add("player 1");

        tui1.gameOver(winners, new HashMap<>());

        TUI tui2 = new TUI();
        tui2.gameOver(winners, new HashMap<>());
    }

    public void testOnSetLastTurn() {
        TUI tui1 = new TUI();
        tui1.setNickname("p2");

        Map<String, Position> playersPosition = new HashMap<>();
        playersPosition.put("p1", Position.FIRST);
        playersPosition.put("p2", Position.SECOND);
        playersPosition.put("p3", Position.THIRD);

        tui1.setPlayersOrder(playersPosition);
        tui1.onSetLastTurn("p2");
    }
}