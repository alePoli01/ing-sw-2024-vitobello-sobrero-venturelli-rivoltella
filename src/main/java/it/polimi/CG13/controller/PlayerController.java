package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.*;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard cardToPlace, boolean isFlipped, Player player, Coordinates xy) {

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
            // check is possible to place the selected card
            board.isPossibleToPlace(xy);
            // add card to the board
            board.addCardToBoard(xy, cardToPlace, isFlipped);
            // removes covered reigns / objects from board map
            board.removeResources(xy);
            // pop card played from hand
            player.handUpdate(cardToPlace);
            // sum reigns / objects
            board.addResource(cardToPlace, isFlipped);
            // update player's scoreboard
            if (!isFlipped) {
                // gold cards gives points differently
                if (cardToPlace.cardType.equals(CardType.GOLD)) {
                    board.setScore(board.getScore() + cardToPlace.getPointsGiven(board, xy));
                } else {
                    board.setScore(board.getScore() + cardToPlace.pointsGiven);
                }
            }
        } catch (CardNotPlacedException | CardStillOnHandException | EdgeNotFreeException e) {
            System.out.println(e.getMessage());
        }
    }

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
        } catch (NotMyTurnException | CardNotFoundException | CardNotAddedToHandException | NoCardsLeftException e) {
            System.out.println(e.getMessage());
       }
    }
}
