package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.TUI.BoardView;
import junit.framework.TestCase;

public class ControllerTest extends TestCase {
    Game game = new Game(2, "test");
    SetupPhase setupPhase;
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    MidPhase midPhase;
    BoardView boardView = new BoardView();

    /*
        PASSED -> SETUP PHASE
     */
    public void testPrepareTable() {
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (Exception e) {
            System.out.println("Error creating");
        }

        this.game.getPlayerList()
                .forEach(player -> player.getHand()
                        .forEach(card -> {
                            card.cardPrinter(false);
                            System.out.println("         " + card.serialNumber);
                        }));
        System.out.println("fine start card");

        this.game.getTable().getGoldCardMap().keySet()
                .forEach(card -> {
                    card.cardPrinter(false);
                    System.out.println("         " + card.serialNumber);
                });
        this.game.getTable().getResourceCardMap().keySet()
                .forEach(card -> {
                    card.cardPrinter(false);
                    System.out.println("         " + card.serialNumber);
                });
    }

    /*
        PASSED -> SETUP PHASE
     */
    public void testPlaceStartCard() {
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (Exception e) {
            System.out.println("Error creating");
        }

        this.setupPhase.placeStartCard(player1, true);
        this.setupPhase.placeStartCard(player2, true);
        assert (this.player1.getBoard().boardMapContainsKeyOfValue(50, 50));
        assert (this.player2.getBoard().boardMapContainsKeyOfValue(50, 50));
        assert (this.player1.getHand().size() == 3);
        assert (this.player2.getHand().size() == 3);
    }

    /*
        PASSED -> MID PHASE
     */
    public void testPlaceCard() {
        this.midPhase = new MidPhase(new Controller(this.game, null, null));
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
        this.boardView.insertCard(50, 50, this.player1.getHand().getFirst().serialNumber, 0, true);
        this.setupPhase.placeStartCard(this.player1, true);
        this.setupPhase.placeStartCard(this.player2, true);

        int X = 49;
        int Y = 49;
        this.player1.setMyTurn(true);
        this.player2.setMyTurn(true);

        // player 1 start card and first resource card
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, X, Y);
        this.boardView.insertCard(Y, X, this.player1.getHand().getFirst().serialNumber, 1, true);
        assert (this.player1.getBoard().boardMapContainsKeyOfValue(X, Y));
        assert (this.player1.getHand().size() == 2);
        this.boardView.printBoard();

        this.midPhase.placeCard(this.player2, this.player2.getHand().getFirst().serialNumber, true, X, Y);
        assert (this.player2.getBoard().boardMapContainsKeyOfValue(X, Y));
        assert (this.player2.getHand().size() == 2);

        // player 1 second resource card
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, X+1, Y-1);
        this.boardView.insertCard(Y-1, X+1, this.player1.getHand().getFirst().serialNumber, 2, true);
        assert (this.player1.getBoard().boardMapContainsKeyOfValue(X+1, Y-1));

        this.boardView.printBoard();
    }

    public void testDrawCard() {
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }

        // insert start card in player1 board
        this.boardView.insertCard(50, 50, this.player1.getHand().getFirst().serialNumber, 0, true);
        this.setupPhase.placeStartCard(this.player1, true);
        this.setupPhase.placeStartCard(this.player2, true);

        // check that both players have 3 cards in each hand
        assert (this.player1.getHand().size() == 3);
        assert (this.player2.getHand().size() == 3);

        // set players position
        this.player1.setPosition(Position.FIRST);
        this.player2.setPosition(Position.SECOND);
        this.midPhase = new MidPhase(new Controller(this.game, null, null));

        // place first card
        PlayableCard cardToPlace = this.player1.getHand().getFirst();
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, 49, 49);
        this.boardView.insertCard(49, 49, this.player1.getHand().getFirst().serialNumber, 1, true);

        // check that the card has been placed and the player doesn't have in his hand
        assert (this.player1.getBoard().boardMapContainsKeyOfValue(49, 49));
        assert (this.player1.getHand().size() == 2);
        assert (!this.player1.getHand().contains(cardToPlace));

        assert (this.game.getTable().getResourceCardMap().size() == 3);
        assert (this.game.getTable().getGoldCardMap().size() == 3);

        boolean flag = true;
        if (flag) {
            PlayableCard cardToDraw = this.player1.getTable().getResourceCardMap().keySet().stream().findAny().orElseThrow();

            System.out.println("Test serial card to draw: " + cardToDraw.serialNumber);
            assert (this.game.getTable().getResourceCardMap().containsKey(cardToDraw));

            // METHOD TO CHECK
            assert (this.player1.isMyTurn());
            this.midPhase.drawCard(player1, cardToDraw.serialNumber);

            // check that the player has the selected card in his hand and that hand size is 3
            assert (this.player1.getHand().contains(cardToDraw));
            assert (this.player1.getHand().size() == 3);

            assert (!this.player1.getTable().getResourceCardMap().containsKey(cardToDraw));
            assert (this.game.getTable().getGoldCardMap().size() == 3);
            assert (this.game.getTable().getResourceCardMap().size() == 3);
            assert (!this.player1.isMyTurn());
        } else {
            PlayableCard cardToDraw = this.player1.getTable().getGoldCardMap().keySet().stream().findAny().orElseThrow();

            System.out.println("Test card to draw: " + cardToDraw.serialNumber);
            assert (this.game.getTable().getGoldCardMap().containsKey(cardToDraw));

            // METHOD TO CHECK
            assert (this.player1.isMyTurn());
            this.midPhase.drawCard(player1, cardToDraw.serialNumber);

            // check that the player has the selected card in his hand and that hand size is 3
            assert (this.player1.getHand().contains(cardToDraw));
            assert (this.player1.getHand().size() == 3);

            assert (!this.player1.getTable().getGoldCardMap().containsKey(cardToDraw));
            assert (this.game.getTable().getResourceCardMap().size() == 3);
            assert (this.game.getTable().getGoldCardMap().size() == 3);
            assert (!this.player1.isMyTurn());
        }
    }

    public void testChoosePrivateObjective() {
    }

    public void testAddPlayerToExistingGame() {
    }
}