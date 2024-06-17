package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import junit.framework.TestCase;

public class SetupPhaseTest extends TestCase {
    Game game = new Game(2, "test");
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    SetupPhase setupPhase;

    private void createGame() {
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (Exception e) {
            fail("Error creating");
        }
    }
    public void testChooseToken() {
        createGame();
        setupPhase.chooseToken(player1, TokenColor.RED);
        try{
            setupPhase.chooseToken(player2, TokenColor.RED);
            assert (game.getPlayerList().getFirst().getTokenColor() == TokenColor.RED);
            assert (game.getPlayerList().get(1).getTokenColor() != TokenColor.RED);
        }catch(Exception e){
            fail(e.getMessage());
        }
    }

    public void testPlaceStartCard() {
        createGame();

        this.setupPhase.placeStartCard(player1, true);
        this.setupPhase.placeStartCard(player2, true);
        assert (this.player1.getBoard().checkListContainsCoordinates(this.player1.getBoard().getBoardMap().keySet(), new Coordinates(50, 50)));
        assert (this.player2.getBoard().checkListContainsCoordinates(this.player2.getBoard().getBoardMap().keySet(), new Coordinates(50, 50)));
        assert (this.player1.getHand().size() == 3);
        assert (this.player2.getHand().size() == 3);
    }
    public void testChoosePrivateObjective() {
    }
}