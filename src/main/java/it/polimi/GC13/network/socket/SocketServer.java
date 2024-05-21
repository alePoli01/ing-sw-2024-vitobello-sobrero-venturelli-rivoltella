package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer implements ServerInterface, Runnable {
    /*
    class that represents the "virtual server" for the client

    clients calls the methods of this class
    the class then creates the messages and sends them to the
    server where they'll be elaborated
     */
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientDispatcherInterface clientDispatcher;
    private boolean connectionOpen = true;
    private String gameName="2"; //used to reconnect to the server automatically
    private String playerName="caio";

    public ClientDispatcherInterface getClientDispatcher() {
        return this.clientDispatcher;
    }

    public String getGameName() {
        return gameName;
    }

    public String getPlayerName() {
        return playerName;
    }

    public boolean isConnectionOpen() {
        return connectionOpen;
    }
    public SocketServer(Socket socket, ClientDispatcherInterface clientDispatcher) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
    }

    @Override
    public void sendMessageFromClient(MessagesFromClient messages) {
        try {
            //uncomment to simulate server crash
            //connectionLost();
            if (!connectionOpen) {
                return;
            }
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            System.out.println(e.getMessage() + "errore nel mandare messaggio al server");
            connectionOpen = false;
        }
    }

    // LISTEN CALLS FROM SERVER
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (connectionOpen) {
            try {
                MessagesFromServer message = (MessagesFromServer) inputStream.readObject();
                executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
            } catch (IOException | ClassNotFoundException e) {
                if (this.connectionOpen) {
                    connectionOpen=false;
                }
            }
        }
        executorService.shutdown();
    }


}