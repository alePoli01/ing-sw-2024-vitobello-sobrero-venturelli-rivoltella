package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

import java.util.List;
import java.util.NoSuchElementException;

public class MidPhase implements GamePhase {
    private final Controller controller;

    public MidPhase(Controller controller) {
        this.controller = controller;
        // set first player to play
        this.controller.getGame().getPlayerList().stream()
                .filter(p -> p.getPosition().equals(Position.FIRST))
                .forEach(p -> p.setMyTurn(true));
        this.controller.getGame().getPlayerList().stream()
                .filter(p -> !p.getPosition().equals(Position.FIRST))
                .forEach(p -> p.setMyTurn(false));
    }

    //controller gets object from network, then calls the method accordingly
    @Override
    public void placeCard(Player player, int serialCardToPlace, boolean isFlipped, int X, int Y) {
        Board board = player.getBoard();

        try {
            // increase players turn
            player.increaseTurnPlayed();
            // gets the playable card from the player's hand
            PlayableCard cardToPlace = player.getHand()
                    .stream()
                    .filter(card -> card.serialNumber == serialCardToPlace)
                    .findFirst()
                    .orElseThrow();
            // check player turn
            player.checkMyTurn();
            // Check player has enough resources to play the goldCard
            if (cardToPlace.cardType.equals(CardType.GOLD) && !isFlipped) {
                board.resourceVerifier(cardToPlace);
            }
            // check if it is possible to place the selected card
            Coordinates xy = board.isPossibleToPlace(X, Y);
            // add card to the board
            board.placeCardToTheBoard(xy, cardToPlace, isFlipped);
            // removes covered reigns / objects from board map
            board.removeResources(X, Y);
            // pop card played from hand
            player.removeFromHand(cardToPlace);
            // sum reigns / objects
            board.addResource(cardToPlace, isFlipped);
            // card gives point only if it is not flipped
            if (!isFlipped) {
                // update player's scoreboard
                player.getTable().setPlayerScore(player, player.getScore() + cardToPlace.getPointsGiven(board, X, Y));
                // check if players has reached 20 points, if so sets game's last turn
                if (player.getScore() >= 20) {
                    player.getGame().setLastRound(player);
                }
            }
        } catch (GenericException | NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
    }

    // draw resource / gold card
    @Override
    public void drawCard(Player player, int serialCardToDraw) {
        try {
            // create card
            PlayableCard cardToDraw = player.getTable().getCardFromTable(serialCardToDraw);
            // check player turn
            player.checkMyTurn();
            // draw the selected card from the table and replace with a new one
            player.getTable().drawCard(cardToDraw);
            // add the selected card to player's hand
            player.addToHand(List.of(cardToDraw));
            // end player's turn
            player.setMyTurn(false);
            // set next player turn to true
            if (player.getGame().getLastRound() == 0 || player.getTurnPlayed() < player.getGame().getLastRound()) {
                System.out.println(player.getNickname() + " has passed and game continues.");
                player.getGame().setPlayerTurn(player);
            } else if (this.checkGameOver(player)) {
                System.out.println(player.getNickname() + " has played his last turn. Game continues.");
            } else {
                System.out.println(player + " has passed and game is over.");
                this.controller.updateController(new EndPhase(this.controller));
                this.controller.getGame().setGameState(GameState.END);
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void newChatMessage(String sender, String receiver, String message) {
        this.controller.getGame().registerChatMessage(sender, receiver, message);
    }

    @Override
    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) {
        existingGame.getObserver().notifyClients(new OnPlayerNotAddedMessage(player.getNickname(), existingGame.getGameName()));
    }

    @Override
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    // player chooses his objective card
    @Override
    public void choosePrivateObjective(Player player, int indexPrivateObjectiveCard) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    /**
     *
     * @param player current player playing
     * @return returns true if there is at least a player that hasn't played his last turn
     */
    private boolean checkGameOver(Player player) {
        return player.getGame().getPlayerList()
                .stream()
                .anyMatch(p -> p.getTurnPlayed() != player.getGame().getLastRound());
    }
}
