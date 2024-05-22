package it.polimi.GC13.model;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.SocketClient;
import junit.framework.TestCase;

import java.util.List;

public class DiskManagerTest extends TestCase {

    public void testWriteOnDisk() throws GenericException {
        String gameName = "test";
        Game game = new Game(2, gameName);
        assert (game.getPlayerList().isEmpty());

        game.setGameState(GameState.MID);
        Player p1 = new Player("p1");
        Player p2 = new Player("p2");
        game.addPlayerToGame(p1);
        game.addPlayerToGame(p2);
        game.giveFirstCards();
        game.getTable().tableSetup();

        game.getPlayerList().getFirst().setMyTurn(true);
        game.getPlayerList().getLast().setMyTurn(false);
        p1.getBoard().placeCardToTheBoard(new Coordinates(50, 50), p1.getHand().getFirst(), true);
        DiskManager diskManager = game.getObserver().getDiskManager();

        PlayableCard card = p1.getTable().getGoldCardMap().keySet().stream().findFirst().get();
        System.out.println("Game deleted");

        game = null;
        p1 = null;
        p2 = null;
        game = diskManager.readFromDisk(gameName);
        p1 = game.getPlayerList().stream().filter(p -> p.getNickname().equals("p1")).findFirst().get();
        p2 = game.getPlayerList().stream().filter(p -> p.getNickname().equals("p2")).findFirst().get();

        assert (p1.isMyTurn());
        assert (!p2.isMyTurn());

        assert (p1.getBoard().boardMapContainsKeyOfValue(50, 50));
        assert (game.getPlayerList().size() == 2);
    }

    public void testReadFromDisk() {
    }
}