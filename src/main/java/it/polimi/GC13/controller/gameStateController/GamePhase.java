package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public interface GamePhase {

    // player chooses his token, it can be done if its token is empty
    void chooseToken(Player player, TokenColor token);

    // player chooses his objective card
    void choosePrivateObjective(Player player, ObjectiveCard card);

    // place start card on the board in default position
    void placeStartCard(Player player, StartCard cardToPlace, boolean isFlipped);

    // place start card on the board in default position
    void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy);

    // draw resource / gold card
    void drawCard(Player player, Table table, PlayableCard cardToDraw);

    // add player to an existing game
    boolean addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException, NicknameAlreadyTakenException;
}
