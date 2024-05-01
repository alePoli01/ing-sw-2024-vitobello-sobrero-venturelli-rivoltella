package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;

import java.io.Serializable;

public interface ControllerInterface {

    void chooseToken(ClientInterface client, TokenColor token);

    void choosePrivateObjective(ClientInterface client, ObjectiveCard card);

    void placeStartCard(ClientInterface client, boolean isFlipped);

    void placeCard(ClientInterface client, Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy);
    
    void drawCard(ClientInterface client, Player player, Table table, PlayableCard cardToDraw);
 }
