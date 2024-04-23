package it.polimi.GC13.network;

import it.polimi.GC13.exception.PlayerNotAddedException;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;

public interface ClientInterface extends Remote {
    void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException;

    void onCheckForExistingGame(int waitingPlayers);

    void onPlayerAddedToGame(int waitingPlayers);

    void poke() throws IOException;
}