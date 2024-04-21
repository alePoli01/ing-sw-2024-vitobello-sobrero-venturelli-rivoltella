package it.polimi.GC13.network;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface ClientInterface extends Remote {
    void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException;

    void onPlayerJoining(List<Player> playerList, String message);

    void onPlayerJoining(boolean noExistingGames);

    void poke() throws IOException;
}