package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import javax.swing.*;

public class JoiningPhase implements GamePhase {

    private Controller controller;

    public void setController(Controller controller) {

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
        System.out.println("perche non runni bastardo");
        existingGame.checkNickname(player.getNickname());
        existingGame.addPlayerToGame(player);
        if (existingGame.numPlayer == existingGame.getCurrNumPlayer()) {
            this.controller.updateController(new SetupPhase(this.controller));
            System.out.println("perche non runni bastardo");
            return 0;
        } else {
            return existingGame.getCurrNumPlayer();
        }
    }
}
