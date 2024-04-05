package it.polimi.GC13.app;

import it.polimi.GC13.model.Server;
import it.polimi.GC13.network.rmi.RMIServer;
import it.polimi.GC13.network.rmi.RMIServerInterface;

import java.io.IOException;
import java.rmi.RemoteException;

public class ServerApp {
    public static void main(String[] args) throws IOException {
        System.out.println("Hello from Server");
        int RMIport = 1234;

        RMIServerInterface rmiServer = new RMIServer(RMIport, new Server());
        rmiServer.startServer();
    }
}
