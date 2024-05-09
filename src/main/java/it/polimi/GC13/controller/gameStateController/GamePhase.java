package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public interface GamePhase {
    // add player to an existing game
    void addPlayerToExistingGame(Player player, Game existingGame, ClientInterface client) throws GenericException;

    // player chooses his token, it can be done if its token is empty
    void chooseToken(Player player, TokenColor token);

    // player chooses his objective card
    void choosePrivateObjective(Player player, int indexPrivateObjectiveCard);

    // place start card on the board in default position
    void placeStartCard(Player player, boolean isFlipped);

    // place start card on the board in default position
    void placeCard(Player player, int serialCardToPlace, boolean isFlipped, int X, int Y);

    // draw resource / gold card
    void drawCard(Player player, int serialCardToDraw);

}
