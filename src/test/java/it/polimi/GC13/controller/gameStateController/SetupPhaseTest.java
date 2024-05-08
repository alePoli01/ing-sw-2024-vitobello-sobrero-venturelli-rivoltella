package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.TUI.BoardView;
import junit.framework.TestCase;

public class SetupPhaseTest extends TestCase {
    Game game = new Game(2, "test");
    SetupPhase setupPhase;
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    MidPhase midPhase;
    BoardView boardView = new BoardView();

    /*
        PASSED
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
        PASSED
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

    }

    public void testChoosePrivateObjective() {
    }

    public void testAddPlayerToExistingGame() {
    }
}