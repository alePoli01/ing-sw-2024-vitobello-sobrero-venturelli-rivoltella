package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

import java.util.NoSuchElementException;

public class MidPhase implements GamePhase {
    private final Controller controller;

    public MidPhase(Controller controller) {
        this.controller = controller;
        // set first player to play
        this.controller.getGame().getPlayerList().stream()
                .filter(p -> p.getPosition().equals(Position.FIRST))
                .forEach(p -> p.setMyTurn(true));
    }

    //controller gets object from network, then calls the method accordingly
    @Override
    public synchronized void placeCard(Player player, int serialCardToPlace, boolean isFlipped, int X, int Y) {
        Board board = player.getBoard();
        // increase players turn
        player.increaseTurnPlayed();

        try {
            PlayableCard cardToPlace = player.getHand()
                    .stream()
                    .filter(card -> card.serialNumber == serialCardToPlace)
                    .findFirst()
                    .orElseThrow();
            // check player turn
            player.checkMyTurn();
            // Check player has enough resources to play the goldCard
            if (cardToPlace.cardType.equals(CardType.GOLD) && isFlipped) {
                board.resourceVerifier(cardToPlace);
            }
            // check if it is possible to place the selected card
            Coordinates xy = board.isPossibleToPlace(X, Y);
            // add card to the board
            board.addCardToBoard(xy, cardToPlace, isFlipped);
            // removes covered reigns / objects from board map
            board.removeResources(X, Y);
            // pop card played from hand
            board.getOwner().removeFromHand(cardToPlace);
            // sum reigns / objects
            board.addResource(cardToPlace, isFlipped);
            // update player's scoreboard
            if (!isFlipped) {
                // gold cards gives points differently
                board.setPlayerScore(board.getPlayerScore() + cardToPlace.getPointsGiven(board, X, Y));
                // check if players has reached 20 points, if so sets game's last turn
                if (board.getPlayerScore() >= 20) {
                    player.getGame().setLastRound(player);
                }
            }
        } catch (GenericException | NoSuchElementException e) {
            System.err.println(e.getMessage());
        }
    }

    // draw resource / gold card
    @Override
    public void drawCard(Player player, int deckIndex, int cardDeckIndex) {
        try {
            // create card
            PlayableCard cardToDraw = player.getTable().getCardFromTable(cardDeckIndex);
            // check player turn
            player.checkMyTurn();
            // draw the selected card from the table and replace with a new one
            player.getTable().drawCard(cardToDraw);
            // add the selected card to player's hand
            player.addToHand(cardToDraw);
            // end player's turn
            player.setMyTurn(false);
            // set next player turn to true
            if (player.getTurnPlayed() < player.getGame().getLastRound()) {
                System.out.println(player + " has passed and game continues");
                player.getGame().setPlayerTurn(player);
            } else {
                System.out.println(player + " has passed and game is over");
                this.controller.updateController(new EndPhase(this.controller));
                this.controller.getGame().setGameState(GameState.END);
            }
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
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
}
