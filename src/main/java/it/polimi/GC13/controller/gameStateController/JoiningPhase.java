package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.PongMessage;
import it.polimi.GC13.network.messages.fromserver.PingMessage;

/**
 * Game phase responsible to add players to the desired game
 */
public class JoiningPhase implements GamePhase {
    /**
     * The {@link Controller} instance managing the current game phase.
     */
    private final Controller controller;

    /**
     * Constructs a {@code JoiningPhase} with the specified Controller.
     *
     * @param controller The Controller instance managing this game phase.
     */
    public JoiningPhase(Controller controller) {
        this.controller = controller;
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void drawCard(Player player, int serialCardToDraw) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void newChatMessage(String sender, String receiver, String message) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public synchronized void addPlayerToExistingGame(Player player, Game workingGame, ClientInterface client) throws GenericException {
        // it adds players to the existing game
        try {
            workingGame.checkNickname(player.getNickname(), player);
            workingGame.getObserver().addListener(client);
            workingGame.addPlayerToGame(player);
        } catch (GenericException e) {
            throw new GenericException("[Nickname: " + player.getNickname() + "] was already chosen");
        }

        if (workingGame.numPlayer == workingGame.getCurrNumPlayer()) {
            this.controller.getLobbyController().getStartedGameMap().put(workingGame.getGameName(), workingGame);
            this.controller.getLobbyController().getJoinableGameMap().remove(workingGame.getGameName(), workingGame);
            workingGame.setGameState(GameState.SETUP);
            this.controller.updateController(new SetupPhase(this.controller));
        }
    }
}
