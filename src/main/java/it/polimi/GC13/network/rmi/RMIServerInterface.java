package it.polimi.GC13.network.rmi;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote{
    void login(ClientInterface client) throws RemoteException;

    void checkForExistingGame(ClientInterface client) throws RemoteException;

    void createNewGame(String playerNickname, int numOfPlayers, String gameName, ClientInterface client) throws RemoteException;

    void addPlayerToGame(String playerNickname, String gameName, ClientInterface client) throws RemoteException;

    void chooseToken(TokenColor tokenColor, ClientInterface client) throws RemoteException;

    void placeStartCard(boolean isFlipped, ClientInterface client) throws RemoteException;

    void placeCard(int serialCardToPlace, boolean isFlipped, int X, int Y, ClientInterface client) throws RemoteException;

    void writeMessage(String sender, String receiver, String message, ClientInterface client) throws RemoteException; //writes message in gamechat

    void drawCard(int serialCardToDraw, ClientInterface client) throws RemoteException;

    void choosePrivateObjectiveCard(int serialPrivateObjectiveCard, ClientInterface client) throws RemoteException;
}
