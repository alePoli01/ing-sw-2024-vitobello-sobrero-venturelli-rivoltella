package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.ObjectiveCard;
import it.polimi.GC13.model.Player;
import junit.framework.TestCase;

public class DealingPhaseTest extends TestCase {

    public void testChoosePrivateObjective() {

        Game game = new Game(2,"gameName");
        Controller controller = new Controller(game, null, null);
        GamePhase gamePhase = new DealingPhase(controller);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);

            player1.setPosition(Position.FIRST);
            player2.setPosition(Position.SECOND);

            game.dealPrivateObjectiveCards();
            ObjectiveCard objectiveCardP1 = player1.getPrivateObjectiveCard().getFirst();
            ObjectiveCard objectiveCardP2 = player2.getPrivateObjectiveCard().get(1);

            gamePhase.choosePrivateObjective(player1, objectiveCardP1.serialNumber);
            gamePhase.choosePrivateObjective(player2, objectiveCardP2.serialNumber);

            assertEquals(objectiveCardP1.serialNumber, player1.getPrivateObjectiveCard().getFirst().serialNumber);
            //after the picking the second card the first one should be removed
            assertEquals(objectiveCardP1.serialNumber, player1.getPrivateObjectiveCard().getFirst().serialNumber);
        } catch (GenericException e) {
            fail(e.getMessage());
        }
    }
}