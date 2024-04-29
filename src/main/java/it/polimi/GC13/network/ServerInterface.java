package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;

public interface ServerInterface {
    // Methods that can be called by the client

    void addPlayerToGame(String nickname, int numOfPlayers, String gameName) throws NicknameAlreadyTakenException;

    void checkForExistingGame();

    void chooseToken(TokenColor tokenColor);

    void placeStartCard(boolean side);

    void writeMessage(); //writes message in gamechat

    void drawCard();
}
