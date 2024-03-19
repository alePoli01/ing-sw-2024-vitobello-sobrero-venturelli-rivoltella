package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.EdgeNotFree;
import it.polimi.CG13.exception.ForbiddenCoordinates;
import it.polimi.CG13.exception.NoResourceAvailable;
import it.polimi.CG13.exception.NotMyTurnException;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard cardToPlace, boolean isFlipped, Player player, Coordinates xy, int targetCardEdge) throws NotMyTurnException, ForbiddenCoordinates, NoResourceAvailable, EdgeNotFree {

        // check player turn
        try {
            player.checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
        } catch (NotMyTurnException e) {
            System.out.println(e.getMessage());
        }

        // check coordinates are allowed
        try {
            xy.evenVerifier();
        } catch (ForbiddenCoordinates e) {
            System.out.println(e.getMessage()); // WrongCoordinates
        }

        // Check player has enough resources to play the goldcard
        if (cardToPlace.getCardType().equals(CardType.GOLD) && isFlipped) {
            try {
                board.resourceVerifier(cardToPlace);
            } catch (NoResourceAvailable e) {
                System.out.println(e.getMessage()); // NoResourceAvailable si può mettere che controlla tutte le risorse
            }
        }

        /*
        try {
            cardToPlace.edgeAvailable(targetCardEdge);
        } catch (Exception e) {
            throw new RuntimeException(e); // unito con metodo sotto
        }
        */

        // check is possible to place the selected card
        try {
            board.isPossibleToPlace(cardToPlace, xy, targetCardEdge);
        } catch (EdgeNotFree e) {
            System.out.println(e.getMessage()); // NoEdgeAvailable (possiamo anche mettere, in base a dove abbiamo errore, cosa non va)
        }

        // card added to the board (eventualmente aggiungere exception per controllo)
        board.addCardToBoard(xy, cardToPlace);

        // update other cards edges and sub resources from board map
        board.edgesUpdate(xy);

        // pop carta giocata dalla mano
        player.handUpdate(cardToPlace);

        // sum risorse / oggetti
        board.addResource(cardToPlace, isFlipped);

        // update player's scoreboard
        board.setScore(board.getScore() + cardToPlace.getPointsGiven());
        // check if win
    }
}
