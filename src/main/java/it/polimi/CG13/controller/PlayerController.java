package it.polimi.CG13.controller;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.model.*;

public class PlayerController {


    //controller gets object from network, then calls the method accordingly
    public void placeCard(Board board, PlayableCard card, boolean isFlipped, Player player, Coordinates xy, int edge) {
        try {
            player.isMyTurn();
        } catch (Exception e) {
            throw new RuntimeException(e); // NotMyTurn
        }

        try {
            xy.evenVerifier();
        } catch (Exception e) {
            throw new RuntimeException(e); // WrongCoordinates
        }

        if (card.getCardType().equals(CardType.GOLD)) {
            try {
                //check risorse nella mano
            } catch (Exception e) {
                throw new RuntimeException(e); // NoResourceAvailable (anche questa possiamo propagarla dalla mano)
            }
        }

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
