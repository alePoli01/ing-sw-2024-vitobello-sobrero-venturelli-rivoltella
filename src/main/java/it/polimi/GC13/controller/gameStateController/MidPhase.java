package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.model.*;

public class MidPhase implements GamePhase {
    private final Controller controller;

    public MidPhase(Controller controller) {
        this.controller = controller;
        // set first player to play
        for (Player player : this.controller.getGame().getPlayerList()) {
            if (player.getPosition().equals(Position.FIRST)) {
                player.setMyTurn(true);
            }
        }
    }

    //controller gets object from network, then calls the method accordingly
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        Board board = player.getBoard();

        // TODO controllo che la carta passata sia in mano al player

        // increase players turn
        player.increaseTurnPlayed();

        try {
            // check player turn
            player.checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
            // check coordinates are allowed
            xy.evenVerifier();
        } catch (NotMyTurnException | ForbiddenCoordinatesException e) {
            System.out.println(e.getMessage()); // WrongCoordinates
        }

        // Check player has enough resources to play the goldCard
        if (cardToPlace.cardType.equals(CardType.GOLD) && isFlipped) {
            try {
                board.resourceVerifier(cardToPlace);
            } catch (NoResourceAvailableException e) {
                System.out.println(e.getMessage()); // Si può mettere che stampa tutte le risorse che non ha, non solo la prima che dà errore
            }
        }

        try {
            // check if it is possible to place the selected card
            board.isPossibleToPlace(xy);
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
        } catch (CardNotPlacedException | CardStillOnHandException | EdgeNotFreeException e) {
            System.out.println(e.getMessage());
        }
    }

    // draw resource / gold card
    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        try {
            // check player turn
            player.checkMyTurn();
            // draw the selected card from the table
            table.drawCard(cardToDraw);
            // add the selected card to player's hand
            player.addToHand(cardToDraw);
            // add new card to the table
            table.getNewCard(cardToDraw);
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
    
    public void chooseToken(Player player, TokenColor token) {
        System.out.println("Token already chosen");
    }

    // player chooses his objective card
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("You cannot change your objective card");
    }

    @Override
    public void placeStartCard(Player player, boolean isFlipped) {
        System.out.println("You cannot replace the start card");
    }

    public void addPlayerToExistingGame(Player player, Game existingGame) {
    }
}
