package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public interface ControllerInterface {

    void chooseToken(ClientInterface client, Player player, TokenColor token);

    void choosePrivateObjective(ClientInterface client, Player player, ObjectiveCard card);

    void placeStartCard(ClientInterface client, Player player, StartCard cardToPlace, boolean isFlipped);

    void placeCard(ClientInterface client, Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy);
    
    void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw);

    boolean addPlayerToExistingGame(ClientInterface client, Player player, Game existingGame) throws PlayerNotAddedException;
 }
