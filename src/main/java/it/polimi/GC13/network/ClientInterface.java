package it.polimi.GC13.network;

import it.polimi.GC13.model.Player;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    Player getPlayer();

    void startRMIConnection() throws IOException, NotBoundException;
}
