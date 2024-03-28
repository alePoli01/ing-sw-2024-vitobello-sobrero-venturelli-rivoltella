package it.polimi.GC13.controller;

import com.google.gson.reflect.TypeToken;
import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.model.*;

import java.util.Arrays;

public class PlayerController {

    // player chooses his objective card
    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        if (player.getObjectiveCard().getFirst().equals(card)) {
            player.getObjectiveCard().remove(1);
        } else {
            player.getObjectiveCard().removeFirst();
        }
    }

    // player chooses his token, it can be done if its token is empty
    public void chooseToken(Player player, TokenColor token) {
        if (player.getToken().describeConstable().isEmpty())
            if (player.getGame().getTable().getTokenColors().contains(token)) {
                player.setToken(token);
                player.getTable().getTokenColors().remove(token);
            }
    }

    // place start card on the board in default position
    public void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped) {
        try {
            player.getBoard().addCardToBoard(null, cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
        } catch (CardNotPlacedException e) {
            System.out.println(e.getMessage());
        }
    }

    //controller gets object from network, then calls the method accordingly
    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {

        try {
            // check player turn
            player.getBoard().getOwner().checkMyTurn(); // Throws NotMyTurn if it's not the player's turn
            // check coordinates are allowed
            xy.evenVerifier();
        } catch (NotMyTurnException | ForbiddenCoordinatesException e) {
            System.out.println(e.getMessage()); // WrongCoordinates
        }

        // Check player has enough resources to play the goldCard
        if (cardToPlace.cardType.equals(CardType.GOLD) && isFlipped) {
            try {
                player.getBoard().resourceVerifier(cardToPlace);
            } catch (NoResourceAvailableException e) {
                System.out.println(e.getMessage()); // Si può mettere che stampa tutte le risorse che non ha, non solo la prima che dà errore
            }
        }

        try {
            // check is possible to place the selected card
            player.getBoard().isPossibleToPlace(xy);
            // add card to the board
            player.getBoard().addCardToBoard(xy, cardToPlace, isFlipped);
            // removes covered reigns / objects from board map
            player.getBoard().removeResources(xy);
            // pop card played from hand
            player.getBoard().getOwner().handUpdate(cardToPlace);
            // sum reigns / objects
            player.getBoard().addResource(cardToPlace, isFlipped);
            // update player's scoreboard
            if (!isFlipped) {
                // gold cards gives points differently
                if (cardToPlace.cardType.equals(CardType.GOLD)) {
                    player.getBoard().setScore(player.getBoard().getScore() + cardToPlace.getPointsGiven(player.getBoard(), xy));
                } else {
                    player.getBoard().setScore(player.getBoard().getScore() + cardToPlace.pointsGiven);
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
        } catch (NotMyTurnException | CardNotFoundException | CardNotAddedToHandException | NoCardsLeftException e) {
            System.out.println(e.getMessage());
       }
    }
}
