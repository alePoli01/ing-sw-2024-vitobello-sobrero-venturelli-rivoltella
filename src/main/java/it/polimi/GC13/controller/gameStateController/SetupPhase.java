package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.exception.inputException.TokenAlreadyChosenException;
import it.polimi.GC13.model.*;

import java.util.ArrayList;
import java.util.List;

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

    public void chooseToken(Player player, TokenColor tokenColor) throws TokenAlreadyChosenException {
        if (player.getToken() == null) {
            if (player.getGame().getTable().getTokenColors().contains(tokenColor)) {
                //color can be taken, assign it to player and remove it from take-able colors
                player.setToken(tokenColor);
                player.getTable().getTokenColors().remove(tokenColor);
            } else {
                //case: color already taken
                ArrayList<TokenColor> tokenColorsList = new ArrayList<>();
                for (TokenColor tc : TokenColor.values()) {
                    if (player.getGame().getTable().getTokenColors().contains(tc)) {
                        tokenColorsList.add(tc);
                    }
                }
                throw new TokenAlreadyChosenException(tokenColor, tokenColorsList);
            }
        }
    }

    // check if condition to next phase is met, if so it updates the Controller
    public void nextPhaseChecker(Player player){
        if (playersPlacedStartCard(player)) {
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

    public int addPlayerToExistingGame(Player player, Game existingGame) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
        return 0;
    }
}
