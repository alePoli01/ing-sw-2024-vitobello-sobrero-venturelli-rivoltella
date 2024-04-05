package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.model.*;

public class DealingPhase implements GamePhase {
    @Override
    public void chooseToken(Player player, TokenColor token) {

    }

    @Override
    // player chooses his objective card
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        if (!(player.getObjectiveCard().size() < 2 || player.getObjectiveCard().isEmpty())) {
            if (player.getObjectiveCard().getFirst().equals(card)) {
                player.getObjectiveCard().remove(1);
            } else {
                player.getObjectiveCard().removeFirst();
            }
        } else {
            System.out.println("Objective card already chosen or not received yet");
        }
    }

    @Override
    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {

    }

    @Override
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {

    }

    @Override
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {

    }

    @Override
    public void updateState(Game game) {
        game.setGameState(GameState.MID);
    }

    @Override
    public void startGame (Game game) throws CardNotAddedToHandException {
        try {
            game.giveStartCard();
            game.giveFirstCards();
            game.setCommonObjectiveCards();
            game.givePrivateObjectiveCards();
            game.setPlayersPosition();
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {

    }
}
