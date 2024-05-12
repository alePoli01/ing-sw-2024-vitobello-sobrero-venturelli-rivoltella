package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;

import java.io.Serializable;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerInterface extends Serializable{
    // Methods that can be called by the client

    void createNewGame(String playerNickname, int numOfPlayers,  String gameName)  ;

    void addPlayerToGame(String playerNickname, String gameName)  ;

    void checkForExistingGame()  ;

    void chooseToken(TokenColor tokenColor)  ;

    void placeStartCard(boolean isFlipped)  ;

    void placeCard(int serialCardToPlace, boolean isFlipped, int X, int Y)  ;

    void writeMessage(String sender, String receiver, String message)  ; //writes message in gamechat

    void drawCard(int serialCardToDraw)  ;

    void choosePrivateObjectiveCard(int serialPrivateObjectiveCard)  ;
}
