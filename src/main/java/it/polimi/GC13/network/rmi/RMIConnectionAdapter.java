package it.polimi.GC13.network.rmi;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.ClientConnectionTimer;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.PongMessage;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.messages.fromserver.PingMessage;
import it.polimi.GC13.network.socket.ClientDispatcher;

import java.io.IOException;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * RMIConnectionAdapter represents an RMI (Remote Method Invocation) adapter that implements both {@link ServerInterface}
 * and {@link ClientInterface}. It acts as a bridge between the client-side and server-side communication using RMI.
 * <p>
 * This adapter maintains an executor service for handling asynchronous tasks related to message sending and processing.
 * It interacts with {@link RMIServerInterface} to register messages from clients and {@link ClientDispatcher} to handle
 * messages from the server.
 */
public class RMIConnectionAdapter extends UnicastRemoteObject implements ServerInterface, ClientInterface {
    /**
     * {@link ExecutorService} for managing asynchronous tasks related to RMI communication.
     */
    private final ExecutorService executorService;

    /**
     * Stub interface for interacting with the remote RMI server.
     */
    public RMIServerInterface serverStub;

    /**
     * Dispatcher for handling messages received from the server.
     */
    private final ClientDispatcher clientDispatcher;

    /**
     * Flag indicating whether the RMI connection is open or closed.
     */
    private boolean connectionOpen = true;

    /**
     * Timer for managing client-side connection timeouts.
     */
    private ClientConnectionTimer clientConnectionTimer;



    /**
     * Constructs an RMIConnectionAdapter with a specified client dispatcher.
     *
     * @param clientDispatcher The client dispatcher to handle messages from the server.
     * @throws RemoteException If an RMI-related communication exception occurs during object export.
     */
    public RMIConnectionAdapter(ClientDispatcher clientDispatcher) throws RemoteException {
        super();
        this.clientDispatcher = clientDispatcher;
        this.executorService = Executors.newCachedThreadPool();
    }

    /**
     * Starts the RMI connection by looking up the server stub in the RMI registry and setting up necessary components.
     *
     * @param hostName          The hostname of the RMI server.
     * @param port              The port number of the RMI server.
     * @param connectionBuilder The connection builder for managing connection-related tasks.
     * @return The server interface for further interaction.
     * @throws IOException If an I/O exception occurs while setting up the connection.
     */
    public ServerInterface startRMIConnection(String hostName, int port, ConnectionBuilder connectionBuilder) throws IOException {
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

    /**
     * Sets up the connection builder for managing connection-related tasks.
     *
     * @param connectionBuilder The connection builder instance to set up.
     */
    public void connectionBuilderSetup(ConnectionBuilder connectionBuilder) {
        this.clientConnectionTimer = new ClientConnectionTimer(this, connectionBuilder);
    }

    /**
     * Sends a message from the client to the server.
     *
     * @param message The message to send.
     */
    private void sendMessage(MessagesFromClient message) {
        if (connectionOpen) {
            try {
                serverStub.registerMessageFromClient(message, this);
            } catch (RemoteException e) {
                if (connectionOpen) {
                    connectionOpen = false;
                    System.out.println("\nError while sending message, starting auto-remapping...");
                }
            }
        }
    }

    @Override
    public void sendMessageFromClient(MessagesFromClient message) {
        if (connectionOpen) {
            executorService.submit(() -> this.sendMessage(message));
        }
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
        if (connectionOpen) {
            if (message instanceof PingMessage) {
                //ping received, answer with pong
                sendMessageFromClient(new PongMessage());
            } else {
                executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
            }
            clientConnectionTimer.stopTimer();
            clientConnectionTimer.startTimer();
        }
    }
}
