package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.model.*;

public class SetupPhase implements GamePhase {

    private final Controller controller;

    public SetupPhase(Controller controller) {
        this.controller = controller;
        this.prepareTable(this.controller.getGame());
    }
    
    private void prepareTable(Game game) {
        try {
            game.getTable().tableSetup();
            game.giveStartCard();
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        try {
            player.getBoard().addCardToBoard(null, cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
            nextPhaseChecker(player);
        } catch (CardNotPlacedException e) {
            System.out.println(e.getMessage());
        }
    }

    // check that all players in the same game positioned the start card
    public boolean playersPlacedStartCard(Player player) {
        for (Player element : player.getGame().getPlayerList()) {
            if (!element.getBoard().containskeyofvalue(50, 50)) {
                return false;
            }
        }
        return true;
    }

    public void chooseToken(Player player, TokenColor token) {
        if (player.getToken().describeConstable().isEmpty()) {
            if (player.getGame().getTable().getTokenColors().contains(token)) {
                player.setToken(token);
                player.getTable().getTokenColors().remove(token);
                nextPhaseChecker(player);
            }
        }
    }

    // check that all players in the same game chose a token
    public boolean playersChoseToken(Player player) {
        for (Player element : player.getGame().getPlayerList()) {
            if (element.getToken().describeConstable().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    // check if conditions to next phase are met, if so it updates the Controller
    public void nextPhaseChecker(Player player){
        if (playersChoseToken(player) && playersPlacedStartCard(player)) {
            this.controller.updateController(new DealingPhase(this.controller));
            this.controller.getGame().setGameState(GameState.DEALING_CARDS);
        }
    }

    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public boolean addPlayerToExistingGame(Player player, Game existingGame) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
        return false;
    }
}
