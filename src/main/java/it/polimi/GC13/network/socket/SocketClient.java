package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Class that represents the "virtual client" for the server calls the methods of this class
 * the class then creates the messages and sends them to the client where they'll be elaborated
 */
public class SocketClient implements ClientInterface, Runnable {
    /**
     * Output stream for sending messages to the server.
     */
    private final ObjectOutputStream outputStream;

    /**
     * Input stream for receiving messages from the server.
     */
    private final ObjectInputStream inputStream;

    /**
     * Dispatcher for routing messages received from the client to the server.
     */
    private final ServerDispatcherInterface serverDispatcher;

    /**
     * Flag indicating if the connection to the client is open.
     */
    private boolean connectionOpen = true;


    /**
     * Constructs a {@code SocketClient} object with the specified socket and server dispatcher.
     *
     * @param socket           The socket representing the network connection to the client.
     * @param serverDispatcher The server dispatcher for routing client messages to the server.
     * @throws IOException If an I/O error occurs while initializing the input and output streams.
     */
    public SocketClient(Socket socket, ServerDispatcher serverDispatcher) throws IOException {
        this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.outputStream.flush();

        this.serverDispatcher = serverDispatcher;
    }


    @Override
    public synchronized void sendMessageFromServer(MessagesFromServer message) {
        try {
            if (!connectionOpen) {
                return;
            }
            outputStream.writeObject(message);
            outputStream.flush();
        } catch (IOException e) {
            this.connectionOpen = false;
            System.out.println("Error sending message: " + e.getMessage());
        }
    }


    @Override
    public boolean isConnectionOpen() {
        return connectionOpen;
    }


    /**
     * Thread used to read messages sent by the client and forward them to the {@link ServerDispatcher}
     * It also reveals when the client isn't connected anymore
     */
    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (connectionOpen) {
            try {
                MessagesFromClient message = (MessagesFromClient) inputStream.readObject();
                executorService.submit(() -> serverDispatcher.sendToControllerDispatcher(message, this));
            } catch (IOException | ClassNotFoundException e) {
                this.connectionOpen = false;
                System.err.println("Client disconnected, cause: " + e.getMessage());
            }
        }
        executorService.shutdown();
    }
}
