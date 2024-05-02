package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public class JoiningPhase implements GamePhase {
    private final Controller controller;

    public JoiningPhase(Controller controller) {
        this.controller = controller;
    }

    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game workingGame, ClientInterface client) {
        // it adds players to the existing game
        try {
            workingGame.getObserver().addListener(client);
            workingGame.checkNickname(player.getNickname(), player);
            workingGame.addPlayerToGame(player);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }

        if (workingGame.numPlayer == workingGame.getCurrNumPlayer()) {
            this.controller.updateController(new SetupPhase(this.controller));
            workingGame.setGameState(GameState.SETUP);
        }
    }
}
