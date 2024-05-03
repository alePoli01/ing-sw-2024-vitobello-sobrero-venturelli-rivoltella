package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;

public interface ServerInterface {
    // Methods that can be called by the client

    void addPlayerToGame(String playerNickname, int numOfPlayers, String gameName);

    void checkForExistingGame();

    void chooseToken(TokenColor tokenColor);

    void placeStartCard(boolean isFlipped);

    void placeCard(int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy);

    void writeMessage(); //writes message in gamechat

    void drawCard();

    void chosePrivateObjectiveCard(int indexPrivateObjectiveCard);
}
