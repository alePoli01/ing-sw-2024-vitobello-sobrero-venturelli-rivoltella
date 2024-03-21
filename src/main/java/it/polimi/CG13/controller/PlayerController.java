package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.*;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard cardToPlace, boolean isFlipped, Player player, Coordinates xy, int targetCardEdge) throws NotMyTurnException, ForbiddenCoordinates, NoResourceAvailable, EdgeNotFree {

        try {
            // check player turn
            player.checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
            // check coordinates are allowed
            xy.evenVerifier();
        } catch (NotMyTurnException | ForbiddenCoordinates e) {
            System.out.println(e.getMessage()); // WrongCoordinates
        }

        // Check player has enough resources to play the goldcard
        if (cardToPlace.getCardType().equals(CardType.GOLD) && isFlipped) {
            try {
                board.resourceVerifier(cardToPlace);
            } catch (NoResourceAvailable e) {
                System.out.println(e.getMessage()); // Si può mettere che stampa tutte le risorse che non ha, non solo la prima che dà errore
            }
        }

        try {
            // check is possible to place the selected card
            board.isPossibleToPlace(cardToPlace, xy, targetCardEdge);
            // add card to the board
            board.addCardToBoard(xy, cardToPlace);
            // update other cards edges and sub resources from board map
            board.edgesUpdate(xy);
            // pop carta giocata dalla mano
            player.handUpdate(cardToPlace);
            // sum risorse / oggetti
            board.addResource(cardToPlace, isFlipped);
            // update player's scoreboard
            board.setScore(board.getScore() + cardToPlace.getPointsGiven());
        } catch (EdgeNotFree | CardNotPlaced | CardStillOnHand e) {
            System.out.println(e.getMessage());
        }
    }

    public void drawCard()
}
