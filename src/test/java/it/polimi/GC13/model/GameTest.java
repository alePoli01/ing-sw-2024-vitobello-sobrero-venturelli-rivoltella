package it.polimi.GC13.model;

import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

import java.util.List;

public class GameTest extends TestCase {

    public void testAddPlayer() {
    }

    public void testParseJSON() {
    }

    public void testDealStartCard() {

        Player player1 = new Player("giocatore1");
        Game game = new Game(1, "test1");
        try {
            game.addPlayerToGame(player1);
        } catch (GenericException e) {
            fail("AddPlayerToGame exception");
        }
        try {
            game.dealStartCard();
            assertNotNull(player1.getHand().getFirst());
        } catch (GenericException e) {
            fail("card not added to hand");
        }
    }

    public void testPlayerPosition() throws GenericException {
        Player player1 = new Player("giocatore1");
        Player player2 = new Player("giocatore2");
        Player player3 = new Player("giocatore3");
        Player player4 = new Player("giocatore4");

        Game game = new Game(4, "test");
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);
            game.addPlayerToGame(player3);
            game.addPlayerToGame(player4);
        } catch (GenericException e) {
            fail("player not added to game");
        }
        List<Player> playerList = game.getPlayerList();
        System.out.println("Start:");
        for (Player p : playerList) {
            System.out.print(p.getNickname() + " ");
        }
        System.out.println("\n");
        game.setPlayersPosition();

        System.out.println("Players positions:");
        for (Player p : playerList) {
            System.out.println(p.getPosition()+" "+p.getNickname());
        }
    }
}