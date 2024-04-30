package it.polimi.GC13.model;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import junit.framework.Assert;
import junit.framework.TestCase;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class GameTest extends TestCase {

    public void testAddPlayer() {
    }

    public void testParseJSON() {
    }

    public void testGiveStartCard() {

        Player player1 = new Player("giocatore1");
        Board board = new Board(player1);
        Game game = new Game(1);
        try {
            game.giveStartCard();
            assertNotNull(player1.getHand().getFirst());
        } catch (CardNotAddedToHandException e) {
            fail("card not added to hand");
        }
    }

    public void testPlayerPosition() throws PlayerNotAddedException {
        Player player1 = new Player("giocatore1");
        Player player2 = new Player("giocatore2");
        Player player3 = new Player("giocatore3");
        Player player4 = new Player("giocatore4");

        Game game = new Game(4);
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);
            game.addPlayerToGame(player3);
            game.addPlayerToGame(player4);
        } catch (PlayerNotAddedException e) {
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