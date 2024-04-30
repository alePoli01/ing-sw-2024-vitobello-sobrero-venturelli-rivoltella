package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.socket.messages.fromserver.OnDealingPrivateObjectiveCardsMessage;

public class DealingPhase implements GamePhase {
    private final Controller controller;

    public DealingPhase(Controller controller) {
        this.controller = controller;
        this.dealCards();
    }

    private void dealCards() {
        try {
            Game game = this.controller.getGame();
            game.giveStartCard();
            game.giveFirstCards();
            game.setCommonObjectiveCards();
            // message from server to client to update tui
            game.givePrivateObjectiveCards();
            //this.controller.notifySpecificClients(new OnDealingPrivateObjectiveCardsMessage());
            game.setPlayersPosition();
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    // player chooses his objective card
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        if (!(player.getObjectiveCard().size() < 2 || player.getObjectiveCard().isEmpty())) {
            if (player.getObjectiveCard().getFirst().equals(card)) {
                player.getObjectiveCard().remove(1);
                if (this.playersChoseObjectiveCard(player)) {
                    this.controller.updateController(new MidPhase(this.controller));
                    this.controller.getGame().setGameState(GameState.MID);
                }
            } else {
                player.getObjectiveCard().removeFirst();
            }
        } else {
            System.out.println("Objective card already chosen or not received yet");
        }
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    // CHECK that all players in the same game chose their own objective card
    public boolean playersChoseObjectiveCard(Player player) {
        for (Player element : player.getGame().getPlayerList()) {
            if (element.getHand().size() == 2) {
                return false;
            }
        }
        return true;
    }

    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }
}
