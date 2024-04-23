package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.model.*;

public class EndPhase implements GamePhase {

    private final Controller controller;

    public EndPhase(Controller controller) {
        this.controller = controller;
        this.setWinner();
    }

    private Player setWinner() {
        finalScoreCalculation();
        Player winner = this.controller.getGame().getPlayerList().getFirst();
        for (Player player : this.controller.getGame().getPlayerList()) {
            if (player.getBoard().getPlayerScore() > winner.getBoard().getPlayerScore()) {
                winner = player;
            }
        }
        return winner;
    }

    private void finalScoreCalculation() {
        for (Player player : this.controller.getGame().getPlayerList()) {
            //set player score = player score + player's objective points(based on his board)
            player.getBoard().setPlayerScore(player.getBoard().getPlayerScore()+player.getObjectiveCard().getFirst().getObjectivePoints(player.getBoard()) );
        }
    }

    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public int addPlayerToExistingGame(Player player, Game existingGame) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
        return 0;
    }
}
