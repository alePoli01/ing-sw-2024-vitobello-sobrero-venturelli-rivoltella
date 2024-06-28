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
 * Class that represents the "virtual server" for the client
 * <p>
 * clients calls the methods of this class
 * the class then creates the messages and sends them to the
 * server where they'll be elaborated
 */
public class SocketServer implements ServerInterface, Runnable {
    /**
     * Input stream for receiving messages from the server.
     */
    private final ObjectInputStream inputStream;


    /**
     * Output stream for sending messages to the server.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Dispatcher for routing messages received from the server to the client.
     */
    private final ClientDispatcher clientDispatcher;

    /**
     * Flag indicating if the connection to the server is open.
     */
    private boolean connectionOpen = true;

    /**
     * Timer for managing the connection state and handling disconnections.
     */
    private final ClientConnectionTimer clientConnectionTimer;




    @Override
    public void setConnectionOpen(boolean connectionOpen) {
        this.connectionOpen = connectionOpen;
    }


    /**
     * Constructs a {@code SocketServer} object with the specified socket, client dispatcher, and connection builder.
     *
     * @param socket            The socket representing the network connection to the server.
     * @param clientDispatcher  The client dispatcher for routing server messages to the client.
     * @param connectionBuilder The connection builder for handling connection events.
     * @throws IOException If an I/O error occurs while initializing the input and output streams.
     */
    public SocketServer(Socket socket, ClientDispatcher clientDispatcher, ConnectionBuilder connectionBuilder) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
        this.clientConnectionTimer = new ClientConnectionTimer(this, connectionBuilder);
    }



    @Override
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

    /**
     * Thread used to read messages sent by the server and forward them to the {@link it.polimi.GC13.network.ClientInterface}
     * It also reveals when the server isn't connected anymore
     */
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
                    System.err.println("Server disconnected, cause: " + e.getMessage());
                }
            }
        }
        executorService.shutdown();
    }
}