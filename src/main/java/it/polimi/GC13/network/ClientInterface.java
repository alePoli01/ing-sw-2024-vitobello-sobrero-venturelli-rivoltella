package it.polimi.GC13.network;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.exception.inputException.InputException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceStartCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.Map;

public interface ClientInterface extends Remote {

    void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException;

    void onPlayerAddedToGame(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);

    void poke() throws IOException;

    void onCheckForExistingGame(Map<String, Game> joinableGameMap, Map<Game, Integer> waitingPlayersMap);

    void onTokenChoiceMessage(TokenColor tokenColor);

    void onDealingCard(int[] availableCards);

    void inputExceptionHandler(InputException e);

    void onPlaceStartCardMessage(OnPlaceStartCardMessage onPlaceStartCardMessage);
}