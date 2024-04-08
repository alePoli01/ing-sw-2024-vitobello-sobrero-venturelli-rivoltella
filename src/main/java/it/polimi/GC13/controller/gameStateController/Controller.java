package it.polimi.GC13.controller.gameStateController;


import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public class Controller implements GamePhase {
    private GamePhase gameController;
    private final Game game;

    // add a new created game to the game manager with its respective controller
    public Controller(Game game) {
        this.gameController = new JoiningPhase();
        this.game = game;
    }

    public Game getGame() {
        return game;
    }

    public void updateController(GamePhase newGameController) {
        this.gameController = newGameController;
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        this.gameController.chooseToken(player, token);
    }

    @Override
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        this.gameController.choosePrivateObjective(player, card);
    }

    @Override
    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        this.gameController.placeStartCard(player, cardToPlace, isFlipped);
    }

    @Override
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.gameController.placeCard(player, cardToPlace, isFlipped, xy);
    }

    @Override
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        this.gameController.drawCard(player, table, cardToDraw);
    }

    @Override
    public void dealCards() throws CardNotAddedToHandException {
        this.gameController.dealCards();
    }

    @Override
    public void prepareTable(Game game) throws CardNotAddedToHandException {
        this.gameController.prepareTable(game);
    }

    @Override
    public boolean addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException {
        return this.gameController.addPlayerToExistingGame(player, existingGame);
    }
}
