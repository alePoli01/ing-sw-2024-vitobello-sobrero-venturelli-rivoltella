package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

public class SetupPhase implements GamePhase {
    private final Controller controller;

    public SetupPhase(Controller controller) {
        this.controller = controller;
        this.prepareTable(this.controller.getGame());
    }

    private void prepareTable(Game game) {
        try {
            game.getTable().tableSetup();
            game.dealStartCard();
        } catch (GenericException e){
            System.err.println(e.getMessage());
        }
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        try {
            PlayableCard cardToPlace = player.getHand().getFirst();
            player.getBoard().addStartCardToBoard(cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
            // pop card played from hand
            player.removeFromHand(cardToPlace);
            // if all Players positioned start card, it updates the controller
            if (playersPlacedStartCard(player)) {
                this.controller.getGame().setGameState(GameState.DEALING_CARDS);
                this.controller.updateController(new DealingPhase(this.controller));
            }
        } catch (GenericException e) {
            System.err.println(e.getMessage());
        }
    }

    // check that all players in the same game positioned the start card
    private synchronized boolean playersPlacedStartCard(Player player) {
        System.out.println("checking place cards...");
        for (Player p : player.getGame().getPlayerList()) {
            if (!p.getBoard().boardMapContainsKeyOfValue(50, 50)) {
                System.err.println(p.getNickname() + " didn't place start card");
                return false;
            }
        }
        System.out.println("All players have placed start card");
        return true;
    }

    public void chooseToken(Player player, TokenColor tokenColor) {
        try {
            player.setTokenColor(tokenColor);
        } catch (GenericException e) {
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, int serialCardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.err.println("I am SETUP PHASE");
        System.err.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        existingGame.getObserver().notifyClients(new OnPlayerNotAddedMessage(player.getNickname(), existingGame.getGameName()));
    }
}
