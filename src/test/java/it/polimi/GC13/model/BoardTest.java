package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

import java.util.LinkedList;

public class BoardTest extends TestCase {

    public void testInsertCards() {
        Game game = new Game(1, "test");
        Deck deck = new Deck();
        Player player = new Player("p1");
        LinkedList<PlayableCard> hand = new LinkedList<>();
        Board board = new Board(player);
        boolean isFlipped = false;

        hand.add(deck.getCard(28));
        hand.add(deck.getCard(31));
        hand.add(deck.getCard(20));

        try {
            game.addPlayerToGame(player);
            System.out.println("Start card");
            board.placeCardToTheBoard(new Coordinates(50, 50), deck.getStartDeck().getFirst(), true);
            System.out.println("First card");
            placeCard(hand, 0, isFlipped, board, player, 49, 49);
            System.out.println("Second card");
            placeCard(hand, 2, isFlipped, board, player, 51, 49);
            System.out.println("Third card");
            placeCard(hand, 1, isFlipped, board, player, 50, 48);
        } catch (GenericException e) {
            System.out.println("Test passed");
            assert (e.getMessage().equals("Forbidden cell 50, 48"));
        }
    }

    private void placeCard(LinkedList<PlayableCard> hand, int position, boolean isFlipped, Board board, Player player, int x, int y) throws GenericException {
        // gets the playable card from the player's hand
        PlayableCard cardToPlace = hand.get(position);
        // Check player has enough resources to play the goldCard
        if (cardToPlace.cardType.equals(CardType.GOLD) && !isFlipped) {
            board.resourceVerifier(cardToPlace);
        }
        // check if it is possible to place the selected card
        Coordinates xy = board.isPossibleToPlace(x, y);
        // add card to the board
        board.placeCardToTheBoard(xy, cardToPlace, isFlipped);
        // removes covered reigns / objects from board map
        board.removeResources(x, y);
        // sum reigns / objects
        board.addResource(cardToPlace, isFlipped);
        // card gives point only if it is not flipped
        if (!isFlipped) {
            // update player's scoreboard
            player.getTable().setPlayerScore(player, cardToPlace.getPointsGiven(board, x, y));
            // check if players has reached 20 points, if so sets game's last turn
            if (player.getScore() >= 20) {
                player.getGame().setLastRound(player);
            }
        }
    }

    public void testIsPossibleToPlace() {
    }

    public void testPlaceCardToTheBoard() {
    }

    public void testRemoveResources() {
    }

    public void testAddResource() {
    }
}