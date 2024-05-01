package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Player;

public interface ServerInterface {
    // Methods that can be called by the client

    void addPlayerToGame(Player player, int numOfPlayers, String gameName);

    void checkForExistingGame();

    void chooseToken(TokenColor tokenColor);

    void placeStartCard(boolean isFlipped);

    void writeMessage(); //writes message in gamechat

    void drawCard();
}
