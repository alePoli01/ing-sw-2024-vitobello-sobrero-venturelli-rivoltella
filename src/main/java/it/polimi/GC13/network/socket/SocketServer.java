package it.polimi.GC13.network.socket;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

/**
    class that represents the "virtual server" for the client

    clients calls the methods of this class
    the class then creates the messages and sends them to the
    server where they'll be elaborated
 */
public class SocketServer implements ServerInterface, Runnable {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientDispatcher clientDispatcher;
    private boolean connectionOpen = true;
    private final ConnectionBuilder connectionBuilder;

    public ClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }

    public SocketServer(Socket socket, ClientDispatcher clientDispatcher, ConnectionBuilder connectionBuilder) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
        this.connectionBuilder = connectionBuilder;
    }


    @Override
    public void sendMessageFromClient(MessagesFromClient messages) {
        try {
            if (!this.connectionOpen) {
                return;
            }
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage() + " errore nel mandare messaggio al server");
        }
    }

    @Override
    public boolean isConnectionOpen() {
        return this.connectionOpen;
    }

    // LISTEN CALLS FROM SERVER
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (connectionOpen) {
            try {
                MessagesFromServer message = (MessagesFromServer) inputStream.readObject();
                executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
            } catch (IOException | ClassNotFoundException e) {
                System.out.println("\nError registering message from Server, trying to remap...");
                this.connectionOpen = false;
                this.connectionBuilder.connectionLost(this, false);
                this.connectionOpen = false;
            }
        }
        executorService.shutdown();
    }
}