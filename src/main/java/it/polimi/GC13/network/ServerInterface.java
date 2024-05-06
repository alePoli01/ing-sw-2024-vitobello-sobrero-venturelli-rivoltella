package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;

public interface ServerInterface {
    // Methods that can be called by the client

    void createNewGame(String playerNickname, int numOfPlayers,  String gameName);

    void addPlayerToGame(String playerNickname, String gameName);

    void checkForExistingGame();

    void chooseToken(TokenColor tokenColor);

    void placeStartCard(boolean isFlipped);

    void placeCard(int serialCardToPlace, boolean isFlipped, int X, int Y);

    void writeMessage(); //writes message in gamechat

    void drawCard(int deckIndex, int cardDeckIndex);

    void chosePrivateObjectiveCard(int indexPrivateObjectiveCard);
}
