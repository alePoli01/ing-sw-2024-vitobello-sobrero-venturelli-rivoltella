package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.model.*;

public class SetupPhase implements GamePhase {

    @Override
    public void updateState(Game game) {
        game.setGameState(GameState.DEALING_CARDS);
    }

    @Override
    public void startGame(Game game) throws CardNotAddedToHandException {
        System.out.println("Prepare Table first");
    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {
        try {
            game.getDeck().shuffleDecks();
            game.getTable().tableSetup();
            game.giveStartCard();
            /*
                TODO capire come passare a dealing phase solo quando la start card è stata posizionata
             */
            this.updateState(game);

        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        try {
            player.getBoard().addCardToBoard(null, cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
            this.updateState(player.getGame());
        } catch (CardNotPlacedException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        if (player.getToken().describeConstable().isEmpty()) {
            if (player.getGame().getTable().getTokenColors().contains(token)) {
                player.setToken(token);
                player.getTable().getTokenColors().remove(token);
            }
        }
    }

    @Override
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {

    }

    @Override
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {

    }

    @Override
    public void choosePrivateObjective(Player player, ObjectiveCard card) {

    }
}
