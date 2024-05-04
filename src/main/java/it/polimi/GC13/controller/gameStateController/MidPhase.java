package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

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
    public void placeCard(Player player, int cardToPlaceHandIndex, boolean isFlipped, int X, int Y) {
        Board board = player.getBoard();
        // increase players turn
        player.increaseTurnPlayed();
        PlayableCard cardToPlace = player.getHand().get(cardToPlaceHandIndex - 1);

        try {
            // check player turn
            player.checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
            // Check player has enough resources to play the goldCard
            if (cardToPlace.cardType.equals(CardType.GOLD) && isFlipped) {
                board.resourceVerifier(cardToPlace);
            }
        } catch (NotMyTurnException | GenericException e) {
            System.out.println(e.getMessage());
        }

        try {
            // check if it is possible to place the selected card
            Coordinates xy = board.isPossibleToPlace(X, Y);
            // add card to the board
            board.addCardToBoard(xy, cardToPlace, isFlipped);
            // removes covered reigns / objects from board map
            board.removeResources(xy);
            // pop card played from hand
            board.getOwner().handUpdate(cardToPlace);
            // sum reigns / objects
            board.addResource(cardToPlace, isFlipped);
            // update player's scoreboard
            if (!isFlipped) {
                // gold cards gives points differently
                board.setPlayerScore(board.getPlayerScore() + cardToPlace.getPointsGiven(board, xy));
                // check if players has reached 20 points, if so sets game's last turn
                if (board.getPlayerScore() >= 20) {
                    player.getGame().setLastRound(player);
                }
            }
        } catch (GenericException e) {
            System.err.println(e.getMessage());
        }
    }

    // draw resource / gold card
    @Override
    public void drawCard(Player player, int deckIndex, int cardDeckIndex) {
        try {
            // create card
            PlayableCard cardToDraw = player.getTable().getCard(cardDeckIndex);
            // check player turn
            player.checkMyTurn();
            // draw the selected card from the table
            player.getTable().drawCard(cardToDraw);
            // add the selected card to player's hand
            player.addToHand(cardToDraw);
            // add new card to the table
            player.getTable().getNewCard(cardToDraw);
            // end player's turn
            player.setMyTurn(false);
            // set next player turn to true
            if (player.getTurnPlayed() < player.getGame().getLastRound()) {
                player.getGame().setPlayerTurn(player);
            } else {
                this.controller.updateController(new EndPhase(this.controller));
                this.controller.getGame().setGameState(GameState.END);
            }
        } catch (NotMyTurnException | CardNotFoundException | CardNotAddedToHandException | NoCardsLeftException e) {
            System.out.println(e.getMessage());
        }
    }

    @Override
    public void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException {
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
