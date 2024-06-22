package it.polimi.GC13.network.socket;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ClientConnectionTimer;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.PongMessage;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.messages.fromserver.PingMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

/**
 * class that represents the "virtual server" for the client
 * <p>
 * clients calls the methods of this class
 * the class then creates the messages and sends them to the
 * server where they'll be elaborated
 */
public class SocketServer implements ServerInterface, Runnable {
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientDispatcher clientDispatcher;
    private boolean connectionOpen = true;

    private final ClientConnectionTimer clientConnectionTimer;

    @Override
    public void setConnectionOpen(boolean connectionOpen) {
        this.connectionOpen = connectionOpen;
    }

    public SocketServer(Socket socket, ClientDispatcher clientDispatcher, ConnectionBuilder connectionBuilder) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
        this.clientConnectionTimer = new ClientConnectionTimer(this, connectionBuilder);
    }

    public ClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }

    @Override
    public void sendMessageFromClient(MessagesFromClient messages) {
        try {
            if (!connectionOpen) {
                return;
            }
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            if (connectionOpen) {
                connectionOpen = false;
                System.out.println("\nError while sending message to server\n" + e.getMessage());
            }
        }
    }

    // LISTEN CALLS FROM SERVER
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (connectionOpen) {
            try {
                MessagesFromServer message = (MessagesFromServer) inputStream.readObject();
                executorService.submit(() -> {
                    if (message instanceof PingMessage) {
                        sendMessageFromClient(new PongMessage());
                    }
                    this.clientDispatcher.registerMessageFromServer(message);
                });
                clientConnectionTimer.stopTimer();
                clientConnectionTimer.startTimer();
            } catch (IOException | ClassNotFoundException e) {
                if (connectionOpen) {
                    connectionOpen = false;
                    System.out.println("\nError while registering message from Server");
                }
            }
        }
        executorService.shutdown();
    }
}