package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

/**
 * Interface representing the various phases and actions that can be performed during a game.
 * Implementations of this interface handle interactions between players, game state, and clients.
 */
public interface GamePhase {

    /**
     * adds the player to the chosen game
     * @param player player that will be added
     * @param existingGame game chosen
     * @param client player's client
     * @throws GenericException thrown if player's nickname has been already chosen
     */
    void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException;

    /**
     * player chooses his token, it can be done if its token is available
     * @param player player that will be added
     * @param token token chosen by the player
     */
    void chooseToken(Player player, TokenColor token);

    /**
     * player chooses his objective card
     * @param player player that wants to perform the action
     * @param serialPrivateObjectiveCard serial number of the private card chosen
     */
    void choosePrivateObjective(Player player, int serialPrivateObjectiveCard);

    /**
     * place start card on the board in default position
     * @param player player that wants to perform the action
     * @param isFlipped side of the card if true back side, if false front side
     */
    void placeStartCard(Player player, boolean isFlipped);

    /**
     * place card on the board in the position chosen by the player
     * @param player player that wants to perform the action
     * @param serialCardToPlace serial card of the card to place
     * @param isFlipped side of the card
     * @param X coordinate x
     * @param Y coordinate y
     */
    void placeCard(Player player, int serialCardToPlace, boolean isFlipped, int X, int Y);

    /**
     * draws the selected card from the Table
     * @param player player that wants to perform the action
     * @param serialCardToDraw serial card of the card to draw
     */
    void drawCard(Player player, int serialCardToDraw);

    /**
     * method used to send message to any player or to everyone
     * @param sender who sends the message
     * @param receiver who receives the message
     * @param message message sent
     */
    void newChatMessage(String sender, String receiver, String message);
}
