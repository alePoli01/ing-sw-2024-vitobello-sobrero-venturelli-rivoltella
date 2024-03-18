package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.exception.ForbiddenCoordinates;
import it.polimi.CG13.exception.NoResourceAvailable;
import it.polimi.CG13.exception.NotMyTurnException;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard card, boolean isFlipped, Player player, Coordinates xy, int edge) {

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

        if (card.getCardType().equals(CardType.GOLD)) {
            try {
                board.resourceVerifier(card);
            } catch (NoResourceAvailable e) {
                throw new RuntimeException(e); // NoResourceAvailable si può mettere che controlla tutte le risorse
            }
        }

        // da continuare

        try {
            card.edgeAvailable(edge);
        } catch (Exception e) {
            throw new RuntimeException(e); // da mettere exception giusta
        }

        try {
            board.isPossibleToPlace();
        } catch (Exception e) {
            throw new RuntimeException(e); // NoEdgeAvailable (possiamo anche mettere, in base a dove abbiamo errore, cosa non va)
        }


        // pop carta giocata dalla mano
        // sum / sub delle risorse / oggetti
        board.setScore(board.getScore() + card.getPointsGiven()); // update player's scoreboard
        // check if win
    }
}
