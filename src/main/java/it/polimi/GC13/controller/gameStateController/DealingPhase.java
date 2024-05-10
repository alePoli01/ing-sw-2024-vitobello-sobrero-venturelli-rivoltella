package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public class DealingPhase implements GamePhase {
    private final Controller controller;
    private int readyPlayers = 0;

    public DealingPhase(Controller controller) {
        this.controller = controller;
        this.dealCards();
    }

    private void dealCards() {
        try {
            Game game = this.controller.getGame();
            game.giveFirstCards();
            game.setCommonObjectiveCards();
            game.dealPrivateObjectiveCards();
            game.setPlayersPosition();
        } catch (GenericException e){
            System.err.println(e.getMessage());
        }
    }

    public void chooseToken(Player player, TokenColor token) {
        System.err.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    // player chooses his objective card
    public void choosePrivateObjective(Player player, int serialPrivateObjectiveCard) {
        try {
            this.readyPlayers++;
            player.setPrivateObjectiveCard(serialPrivateObjectiveCard, readyPlayers);
            if (this.playersChoseObjectiveCard(player)) {
                this.controller.getGame().setGameState(GameState.MID);
                this.controller.updateController(new MidPhase(this.controller));
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    // CHECK that all players in the same game chose their own objective card
    private synchronized boolean playersChoseObjectiveCard(Player player) {
        for (Player p : player.getGame().getPlayerList()) {
            if (p.getPrivateObjectiveCard().size() == 2) {
                System.err.println(p.getNickname() + " hasn't chosen objective card yet.");
                return false;
            }
        }
        return true;
    }

    public void drawCard(Player player, int serialCardToDraw) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState());
    }

    @Override
    public void registerMessage(String sender, String receiver, String message) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState());
    }
}
