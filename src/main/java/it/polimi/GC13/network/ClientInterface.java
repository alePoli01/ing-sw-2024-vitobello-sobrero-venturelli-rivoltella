package it.polimi.GC13.network;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Game;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.util.Map;

public interface ClientInterface extends Remote {
    void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException;

    void onPlayerAddedToGame(int waitingPlayers);

    void poke() throws IOException;

    void onCheckForExistingGame(Map<String, Game> joinableGameMap, Map<Game, Integer> waitingPlayersMap);

    void exceptionHandler(Exception e);
}