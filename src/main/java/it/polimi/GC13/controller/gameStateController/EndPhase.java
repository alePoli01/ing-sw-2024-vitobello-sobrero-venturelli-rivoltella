package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.util.Set;

/**
 * Game phase responsible to calculate the winner
 */
public class EndPhase implements GamePhase {
    /**
     * The {@link Controller} instance managing the current game phase.
     */
    private final Controller controller;

    /**
     * Constructs a {@code EndPhase} with the specified controller.
     *
     * @param controller The {@link Controller} instance managing this game phase.
     */
    public EndPhase(Controller controller) {
        this.controller = controller;
        Set<String> winner = this.controller.getGame().setWinner();
        System.out.println("GAME OVER -> Winner is " + winner);
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void drawCard(Player player, int serialCardToDraw) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void newChatMessage(String sender, String receiver, String message) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }
}
