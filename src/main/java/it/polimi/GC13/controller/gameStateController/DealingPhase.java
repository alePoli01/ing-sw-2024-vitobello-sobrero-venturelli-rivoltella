package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

/**
 * Game phase responsible to deal cards and objective cards to players
 */
public class DealingPhase implements GamePhase {
    /**
     * The {@link Controller} instance managing the current game phase.
     */
    private final Controller controller;

    /**
     * Counter to track how many players have chosen their private objective card.
     */
    private int readyPlayers = 0;

    /**
     * Constructs a {@code DealingPhase} with the specified Controller and initiates the dealing of cards.
     *
     * @param controller The Controller instance managing this game phase.
     */
    public DealingPhase(Controller controller) {
        this.controller = controller;
        this.dealCards();
    }

    /**
     * Deals initial cards and sets up the game table.
     */
    private void dealCards() {
        try {
            Game game = this.controller.getGame();
            game.giveFirstCards();
            game.getTable().tableSetup();
            game.setCommonObjectiveCards();
            game.dealPrivateObjectiveCards();
            game.setPlayersPosition();
        } catch (GenericException e){
            System.err.println(e.getMessage());
        }
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void choosePrivateObjective(Player player, int serialPrivateObjectiveCard) {
        try {
            this.readyPlayers++;
            player.setPrivateObjectiveCard(serialPrivateObjectiveCard, readyPlayers);
            if (this.playersChoseObjectiveCard(player)) {
                // set first player to play
                this.controller.getGame().setGameState(GameState.MID);
                this.controller.getGame().getPlayerList().stream()
                        .filter(p -> p.getPosition().equals(Position.FIRST))
                        .forEach(p -> p.setMyTurn(true));
                this.controller.getGame().getPlayerList().stream()
                        .filter(p -> !p.getPosition().equals(Position.FIRST))
                        .forEach(p -> p.setMyTurn(false));

                this.controller.updateController(new MidPhase(this.controller));
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    @Override
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }

    /**
     * Checks that all players in the same game chose their own objective card
     * @param player player that has chosen the objective card
     * @return true if he is the last one, false if there is someone missing
     */
    private synchronized boolean playersChoseObjectiveCard(Player player) {
        for (Player p : player.getGame().getPlayerList()) {
            if (p.getPrivateObjectiveCard().size() == 2) {
                System.err.println(p.getNickname() + " hasn't chosen objective card yet.");
                return false;
            }
        }
        return true;
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
    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        System.out.println("Error, game is in " + this.controller.getGame().getGameState() + " phase.");
    }
}
