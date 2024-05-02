package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public class DealingPhase implements GamePhase {
    private final Controller controller;

    public DealingPhase(Controller controller) {
        this.controller = controller;
        this.dealCards();
    }

    private void dealCards() {
        try {
            Game game = this.controller.getGame();
            game.giveFirstCards();
            game.setCommonObjectiveCards();
            game.dealPrivateObjectiveCards();
            game.setPlayersPosition();
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    // player chooses his objective card
    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        try {
            player.setPrivateObjectiveCard(indexPrivateObjectiveCard);
            if (this.playersChoseObjectiveCard(player)) {
                this.controller.updateController(new MidPhase(this.controller));
                this.controller.getGame().setGameState(GameState.MID);
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    // CHECK that all players in the same game chose their own objective card
    private boolean playersChoseObjectiveCard(Player player) {
        for (Player element : player.getGame().getPlayerList()) {
            if (element.getHand().size() == 2) {
                return false;
            }
        }
        return true;
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
}
