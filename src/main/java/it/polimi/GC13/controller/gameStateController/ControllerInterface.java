package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

public interface ControllerInterface {

    void chooseToken(ClientInterface client, TokenColor token);

    void choosePrivateObjective(ClientInterface client, int privateObjectiveCardIndex);

    void placeStartCard(ClientInterface client, boolean isFlipped);

    void placeCard(ClientInterface client, int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy);
    
    void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw);
 }
