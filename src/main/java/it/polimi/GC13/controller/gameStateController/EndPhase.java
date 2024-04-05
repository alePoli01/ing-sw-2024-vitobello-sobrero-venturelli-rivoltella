package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.model.*;

public class EndPhase implements GamePhase {
    @Override
    public void chooseToken(Player player, TokenColor token) {

    }

    @Override
    public void choosePrivateObjective(Player player, ObjectiveCard card) {

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
        System.out.println("Game over, restart a new one");
    }

    @Override
    public void startGame(Game game) throws CardNotAddedToHandException {

    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {

    }
}
