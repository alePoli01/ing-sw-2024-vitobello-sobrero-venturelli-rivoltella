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
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
    
    public void drawCard(Player player, int deckIndex, int cardDeckIndex) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public synchronized void addPlayerToExistingGame(Player player, Game workingGame, ClientInterface client) throws GenericException {
        // it adds players to the existing game
        try {
            workingGame.checkNickname(player.getNickname(), player);
            workingGame.getObserver().addListener(client);
            workingGame.addPlayerToGame(player);
        } catch (GenericException e) {
            throw new GenericException("Nickname: " + player.getNickname() + " was already choose");
        }

        if (workingGame.numPlayer == workingGame.getCurrNumPlayer()) {
            this.controller.getLobbyController().getStartedGameMap().put(workingGame.getGameName(), workingGame);
            this.controller.getLobbyController().getJoinableGameMap().remove(workingGame.getGameName(), workingGame);
            player.getGame().getPlayerList()
                    .forEach(p -> System.out.println(p.getNickname() + " joined the game"));
            System.out.println(player.getGame().getObserver().listenerList.size());
            workingGame.setGameState(GameState.SETUP);
            this.controller.updateController(new SetupPhase(this.controller));
        }
    }
}
