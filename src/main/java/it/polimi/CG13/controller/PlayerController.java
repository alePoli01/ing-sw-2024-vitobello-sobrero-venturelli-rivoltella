package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.EdgeNotFree;
import it.polimi.CG13.exception.ForbiddenCoordinates;
import it.polimi.CG13.exception.NoResourceAvailable;
import it.polimi.CG13.exception.NotMyTurnException;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard cardToPlace, boolean isFlipped, Player player, Coordinates xy, int targetCardEdge) {

        try {
            player.checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
        } catch (NotMyTurnException e) {
            throw new RuntimeException();
        }

        try {
            xy.evenVerifier();
        } catch (ForbiddenCoordinates e) {
            throw new RuntimeException(); // WrongCoordinates
        }

        if (cardToPlace.getCardType().equals(CardType.GOLD) && isFlipped) {
            try {
                board.resourceVerifier(cardToPlace);
            } catch (NoResourceAvailable e) {
                throw new RuntimeException(e); // NoResourceAvailable si può mettere che controlla tutte le risorse
            }
        }

        /*
        try {
            cardToPlace.edgeAvailable(targetCardEdge);
        } catch (Exception e) {
            throw new RuntimeException(e); // unito con metodo sotto
        }
        */

        try {
            board.isPossibleToPlace(cardToPlace, xy, targetCardEdge);
        } catch (EdgeNotFree e) {
            throw new RuntimeException(e); // NoEdgeAvailable (possiamo anche mettere, in base a dove abbiamo errore, cosa non va)
        }

        // card added to the board (eventualmente aggiungere exception per controllo)
        board.addCardToBoard(xy, cardToPlace);

        // pop carta giocata dalla mano
        player.handUpdate(cardToPlace);

        // sum / sub delle risorse / oggetti
        board.resourceUpdate(cardToPlace, isFlipped);

        // update player's scoreboard
        board.setScore(board.getScore() + cardToPlace.getPointsGiven());
        // check if win
    }
}
