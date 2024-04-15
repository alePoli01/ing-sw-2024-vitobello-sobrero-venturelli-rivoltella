package it.polimi.GC13.network;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {
    //methods that the client invokes to play
    Player getPlayer();

    void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException;
    /**/

}
