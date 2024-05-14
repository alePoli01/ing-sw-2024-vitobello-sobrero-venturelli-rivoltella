package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.util.List;

public class EndPhase implements GamePhase {
    private final Controller controller;

    public EndPhase(Controller controller) {
        this.controller = controller;
        String winner = this.controller.getGame().setWinner();
        System.out.println("GAME OVER -> Winner is " + winner);
    }

    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, int serialCardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void newChatMessage(String sender, String receiver, String message) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
}
