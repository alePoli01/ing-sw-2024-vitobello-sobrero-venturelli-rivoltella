package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

import java.util.LinkedList;

public class BoardTest extends TestCase {
    Game game = new Game(1, "test");
    Deck deck = new Deck();
    Player player = new Player("p1");
    Board board = new Board(player);
    LinkedList<PlayableCard> hand = new LinkedList<>();

    public void testInsertCards() {
        boolean isFlipped = false;

        hand.add(deck.getCard(18));//cardA
        hand.add(deck.getCard(36));//cardB
        hand.add(deck.getCard(51));//cardC
        //Print cards

        for (int i = -1; i < 6; i++) {
            if (i == -1) {
                System.out.println(hand.get(0).serialNumber + "\t\t\t\t\t" + hand.get(1).serialNumber + "\t\t\t\t\t" + hand.get(2).serialNumber);
            } else {
                hand.get(0).linePrinter(0, i, false);
                hand.get(1).linePrinter(0, i, false);
                hand.get(2).linePrinter(0, i, false);
                System.out.println();
            }

        }

        for (int i = 0; i < 6; i++) {
            //print starter card
            deck.getCard(83).linePrinter(0, i, false);
            System.out.println();
        }
        //choose coordinates
        LinkedList<Coordinates> coordinatesList = new LinkedList<>();
        coordinatesList.add(new Coordinates(49, 51));
        coordinatesList.add(new Coordinates(48, 50));
        coordinatesList.add(new Coordinates(49, 49));

        try {
            //place cards on the board
            game.addPlayerToGame(player);
            System.out.println("Start card");
            board.placeCardToTheBoard(new Coordinates(50, 50), deck.getCard(83), true);
            board.addResource(deck.getCard(83), true);

            System.out.println("Trying to place Card: " + hand.get(2).serialNumber);
            try {
                placeCard(hand, 2, isFlipped, board, player, coordinatesList.get(2).getX(), coordinatesList.get(2).getY());
                fail("Exception not thrown");
            } catch (GenericException e) {
                System.out.println("Exception thrown while adding gold card #"+hand.get(2).serialNumber+" as expected\n");
            }

            int i = 0;
            for (PlayableCard card : hand) {
                System.out.println("Card: " + card.serialNumber);
                placeCard(hand, i, isFlipped, board, player, coordinatesList.get(i).getX(), coordinatesList.get(i).getY());
                i++;
            }
        } catch (GenericException e) {
            fail("Exception thrown " + e.getMessage());
        }
        coordinatesList.forEach((cardCoordinates) ->
                assertTrue(board.getBoardMap().keySet().stream().anyMatch((coordinates) -> coordinates.equals(cardCoordinates))));

        hand.removeFirst();
        hand.addFirst(deck.getCard(19));
        try {
            placeCard(hand, 0, isFlipped, board, player, 50, 50);
            fail("Exception not thrown");
        } catch (GenericException e) {
            System.out.println("Exception thrown while adding card on (50, 50) as expected");
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
        if (cardToPlace.cardType.equals(CardType.STARTER) && isFlipped) {
            board.removeResources(x, y);
        } else if (!isFlipped) {
            board.removeResources(x, y);
        }
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

    public void testSurroundingCardsNumber() {
        testInsertCards();
        assertEquals(2, board.surroundingCardsNumber(50, 50));
        assertEquals(2, board.surroundingCardsNumber(49, 51));
    }
}