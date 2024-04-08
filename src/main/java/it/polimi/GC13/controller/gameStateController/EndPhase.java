package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.model.*;

public class EndPhase implements GamePhase {

    private final Controller controller;

    public EndPhase(Controller controller) {
        this.controller = controller;
    }

    public Player setWinner() {
        finalScoreCalculation();
        Player winner = this.controller.getGame().getPlayerList().getFirst();
        for (Player player : this.controller.getGame().getPlayerList()) {
            if (player.getBoard().getPlayerScore() > winner.getBoard().getPlayerScore()) {
                winner = player;
            }
        }
        return winner;
    }

    public void finalScoreCalculation() {
        for (Player player : this.controller.getGame().getPlayerList()) {
            /*
            TODO invocation to methods to calculate players final score
             */
        }
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void dealCards() throws CardNotAddedToHandException {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public boolean addPlayerToExistingGame(Player player, Game existingGame) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
        return false;
    }
}
