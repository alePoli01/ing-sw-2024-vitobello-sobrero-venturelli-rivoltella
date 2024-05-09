package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public class EndPhase implements GamePhase {
    private final Controller controller;

    public EndPhase(Controller controller) {
        this.controller = controller;
        System.out.println("Winner is " + this.setWinner());
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
            player.getBoard().setPlayerScore(player.getBoard().getPlayerScore()+player.getPrivateObjectiveCard().getFirst().getObjectivePoints(player.getBoard()) );
        }
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

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
}
