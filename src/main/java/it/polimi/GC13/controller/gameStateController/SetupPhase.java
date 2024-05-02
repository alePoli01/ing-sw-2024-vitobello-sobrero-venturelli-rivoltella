package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

import java.util.ArrayList;
import java.util.List;

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
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        try {
            PlayableCard cardToPlace = player.getHand().getFirst();
            player.getBoard().addCardToBoard(new Coordinates(50, 50), cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
            // if all Players positioned start card, it updates the controller
            if (playersPlacedStartCard(player)) {
                this.controller.getGame().setGameState(GameState.DEALING_CARDS);
                this.controller.updateController(new DealingPhase(this.controller));
            }
        } catch (CardNotPlacedException e) {
            System.out.println(e.getMessage());
        }
    }

    // check that all players in the same game positioned the start card
    private boolean playersPlacedStartCard(Player player) {
        for (Player p : player.getGame().getPlayerList()) {
            if (!p.getBoard().containsKeyOfValue(50, 50)) {
                return false;
            }
        }
        return true;
    }

    public void chooseToken(Player player, TokenColor tokenColor) {
        try {
            player.setTokenColor(tokenColor);
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        existingGame.getObserver().notifyClients(new OnPlayerNotAddedMessage(player.getNickname(), existingGame.getGameName()));
    }
}
