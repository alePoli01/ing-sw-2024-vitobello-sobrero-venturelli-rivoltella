package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public class JoiningPhase implements GamePhase {
    private final Controller controller;

    public JoiningPhase(Controller controller) {
        this.controller = controller;
    }


    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public int addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException, NicknameAlreadyTakenException {
        // it adds players to the existing game
        existingGame.checkNickname(player.getNickname());
        existingGame.addPlayerToGame(player);
        //System.out.println("player list:" + existingGame.getPlayerList());
        if (existingGame.numPlayer == existingGame.getCurrNumPlayer()) {
            this.controller.updateController(new SetupPhase(this.controller));
            return 0;
        } else {
            return existingGame.getCurrNumPlayer();
        }
    }
}
