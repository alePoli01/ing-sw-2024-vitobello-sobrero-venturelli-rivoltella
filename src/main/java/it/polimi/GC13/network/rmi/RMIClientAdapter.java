package it.polimi.GC13.network.rmi;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class RMIClientAdapter extends UnicastRemoteObject implements ServerInterface {
    private final ExecutorService executorService;
    public RMIServerInterface serverStub;
    private final ClientInterface rmiClientImpl;

    public RMIClientAdapter(ClientInterface rmiClientImpl) throws RemoteException {
        super();
        this.executorService = Executors.newCachedThreadPool();
        this.rmiClientImpl = rmiClientImpl;
    }

    /*
    clientAdapter: implements ServerInterface, view uses the adapter to ignore the remote exception (view's code shouldn't change regardless of connection type)

    RMIServer: implements RMIServerInterface, identical to ServerInterface but requires the client
    */
    public ServerInterface startRMIConnection(String hostName, int port) throws RemoteException {
        System.out.println("Starting RMI connection to " + hostName + ":" + port + " ...");
        System.out.println("\t\tgetting registry");
        Registry registry = LocateRegistry.getRegistry(hostName, port);
        try {
            System.out.println("\t\tgetting server stub");
            this.serverStub = (RMIServerInterface) registry.lookup("server");
        } catch (RemoteException | NotBoundException e) {
            System.err.println("Registry Lookup for server stub failed.");
            System.exit(-1);
        }
        return this;
    }

    @Override
    public void sendMessageFromClient(MessagesFromClient message) {
        //serverStub.registerMessageFromClient(message);
    }
}
