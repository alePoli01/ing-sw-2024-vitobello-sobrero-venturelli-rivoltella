package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public class JoiningPhase implements GamePhase {

    private Controller controller;

    public void setController(Controller controller) {
        this.controller = controller;
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
        System.out.println("Game has been already created.");
    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {
        System.out.println("Game already started." + game.getCurrNumPlayer() + "more players are needed to begin dealing phase");
    }

    @Override
    public boolean addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException {
        // it adds players to the existing game
        existingGame.addPlayerToGame(player);
        if (existingGame.numPlayer == existingGame.getCurrNumPlayer()) {
            this.controller.updateController(new SetupPhase(this.controller));
            return false;
        } else {
            return true;
        }
    }
}
