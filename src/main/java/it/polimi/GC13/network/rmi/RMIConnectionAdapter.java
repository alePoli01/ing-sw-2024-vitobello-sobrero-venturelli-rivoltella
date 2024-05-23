package it.polimi.GC13.network.rmi;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.socket.ClientDispatcher;

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

    public RMIConnectionAdapter(ClientDispatcher clientDispatcher) throws RemoteException {
        super();
        this.clientDispatcher = clientDispatcher;
        this.executorService = Executors.newCachedThreadPool();
    }

    /*
        clientAdapter: implements ServerInterface, view uses the adapter to ignore the remote exception (view's code shouldn't change regardless of connection type)
        RMIServer: implements RMIServerInterface, identical to ServerInterface but requires the client
    */
    public ServerInterface startRMIConnection(String hostName, int port) throws RemoteException {
        Registry registry = LocateRegistry.getRegistry(hostName, port);
        try {
            this.serverStub = (RMIServerInterface) registry.lookup("server");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Registry Lookup for server stub failed.");
            System.exit(-1);
        }
        return this;
    }

    @Override
    public void sendMessageFromClient(MessagesFromClient message) {
        try {
            serverStub.registerMessageFromClient(message, this);
        } catch (RemoteException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean isConnectionOpen() {
        return this.connectionOpen;
    }

    @Override
    public ClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }

    @Override
    public void sendMessageFromServer(MessagesFromServer message) throws RemoteException {
        executorService.submit(() -> {
            this.clientDispatcher.registerMessageFromServer(message);
        });
    }
}
