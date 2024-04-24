package it.polimi.GC13.controller.gameStateController;


import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public class Controller implements GamePhase {
    private GamePhase gameController;
    private final Game game;
    private LobbyController lobbyController;

    // add a new created game to the game manager with its respective controller
    public Controller(Game game, LobbyController lobbyController) {
        this.gameController = new JoiningPhase(this);
        this.game = game;
        this.lobbyController = lobbyController;
    }

    public Game getGame() {
        return game;
    }

    public GamePhase getGameController() {
        return gameController;
    }

    public void updateController(GamePhase newGameController) {
        this.gameController = newGameController;
    }

    public void chooseToken(Player player, TokenColor token) {
        this.gameController.chooseToken(player, token);
    }

    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        this.gameController.choosePrivateObjective(player, card);
    }

    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        this.gameController.placeStartCard(player, cardToPlace, isFlipped);
    }

    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        this.gameController.placeCard(player, cardToPlace, isFlipped, xy);
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        this.gameController.drawCard(player, table, cardToDraw);
    }

    public int addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException, NicknameAlreadyTakenException {
        System.out.println("perche non runni bastardo controller");
        return this.gameController.addPlayerToExistingGame(player, existingGame);
    }
}
