package it.polimi.GC13.network.rmi;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RMIServerInterface extends Remote {

    void startServer() throws RemoteException;
}
