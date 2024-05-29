package it.polimi.GC13.network.rmi;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIConnectionAdapter extends UnicastRemoteObject implements ServerInterface, ClientInterface {
    private final ExecutorService executorService;
    public RMIServerInterface serverStub;
    private final ClientDispatcher clientDispatcher;
    private final ConnectionBuilder connectionBuilder;
    private boolean connectionOpen = true;
    private Timer timer;

    public RMIConnectionAdapter(ClientDispatcher clientDispatcher, ConnectionBuilder connectionBuilder) throws RemoteException {
        super();
        this.connectionBuilder = connectionBuilder;
        this.clientDispatcher = clientDispatcher;
        this.executorService = Executors.newCachedThreadPool();
        this.startTimer();
    }

    public ServerInterface startRMIConnection(String hostName, int port) throws IOException {
        Registry registry = LocateRegistry.getRegistry(hostName, port);
        try {
            this.serverStub = (RMIServerInterface) registry.lookup("server");
            this.serverStub.createConnection(this);
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Registry Lookup for server stub failed.");
            throw new IOException();
        }
        return this;
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
                this.connectionBuilder.connectionLost(this, false);
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
    public void sendMessageFromServer(MessagesFromServer message) throws RemoteException {
        executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
        this.stopTimer();
        this.startTimer();
    }

    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                System.err.println("\ntimer's run out");
                RMIConnectionAdapter.this.connectionBuilder.connectionLost(RMIConnectionAdapter.this, false);
            }
        };
        timer.schedule(timerTask, 6000);
    }
    private void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }
}
