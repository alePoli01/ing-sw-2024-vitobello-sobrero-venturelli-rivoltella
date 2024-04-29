package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.model.*;

import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

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

    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game workingGame) throws PlayerNotAddedException, NicknameAlreadyTakenException {
        // it adds players to the existing game
        workingGame.checkNickname(player.getNickname(), player);
        workingGame.addPlayerToGame(player);
        if (workingGame.numPlayer == workingGame.getCurrNumPlayer()) {
            this.controller.updateController(new SetupPhase(this.controller));
            workingGame.setGameState(GameState.SETUP);
        }
        this.controller.notifyClients(new OnPlayerAddedToGameMessage(workingGame.getCurrNumPlayer(), workingGame.numPlayer));
    }
}
