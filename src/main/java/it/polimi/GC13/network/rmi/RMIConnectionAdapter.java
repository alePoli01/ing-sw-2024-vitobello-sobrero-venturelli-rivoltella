package it.polimi.GC13.network.rmi;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ConnectionTimer;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.socket.ClientDispatcher;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIConnectionAdapter extends UnicastRemoteObject implements ServerInterface, ClientInterface {
    private final ExecutorService executorService;
    public RMIServerInterface serverStub;
    private final ClientDispatcher clientDispatcher;
    private boolean connectionOpen = true;
    private ConnectionTimer connectionTimer;

    public RMIConnectionAdapter(ClientDispatcher clientDispatcher) throws RemoteException {
        super();
        this.clientDispatcher = clientDispatcher;
        this.executorService = Executors.newCachedThreadPool();

    }

    public ServerInterface startRMIConnection(String hostName, int port,ConnectionBuilder connectionBuilder) throws IOException {
        Registry registry = LocateRegistry.getRegistry(hostName, port);
        try {
            this.serverStub = (RMIServerInterface) registry.lookup("server");
            this.serverStub.createConnection(this);
            connectionBuilderSetup(connectionBuilder);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Registry Lookup for server stub failed.");
            throw new IOException();
        }
        return this;
    }
    public void connectionBuilderSetup(ConnectionBuilder connectionBuilder) {
        this.connectionTimer = new ConnectionTimer(this,connectionBuilder);

    }

    private void sendMessage(MessagesFromClient message) {
        if (!connectionOpen) {
            return;
        }
        try {
            serverStub.registerMessageFromClient(message, this);
        } catch (RemoteException e) {
            if (connectionOpen) {
                connectionOpen = false;
                System.out.println("\nError while sending message, starting auto-remapping...");
            }
        }
    }
    @Override
    public void sendMessageFromClient(MessagesFromClient message) {
        executorService.submit(() -> this.sendMessage(message));
    }

    @Override
    public boolean isConnectionOpen() throws RemoteException {
        return this.connectionOpen;
    }

    @Override
    public ClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }

    @Override
    public void setConnectionOpen(boolean connectionOpen) {
        this.connectionOpen = connectionOpen;
    }

    @Override
    public void sendMessageFromServer(MessagesFromServer message) throws RemoteException {
            if(connectionOpen) {
                executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
                connectionTimer.stopTimer();
                connectionTimer.startTimer();
            }
    }
}
