package it.polimi.GC13.network;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.model.Player;

import java.rmi.RemoteException;

public interface ServerInterface {
    //methods that can be called by the client

    void addPlayerToGame(String nickname,int numOfPlayers) throws NicknameAlreadyTakenException;
    void checkForExistingGame();
    void quitGame();

    void writeMessage();//writes message in gamechat

    void drawCard();
}
