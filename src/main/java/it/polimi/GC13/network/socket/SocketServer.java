package it.polimi.GC13.network.socket;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.*;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.*;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;
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
    private Timer timer;

    public SocketServer(Socket socket, ClientDispatcher clientDispatcher, ConnectionBuilder connectionBuilder) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
        this.connectionBuilder = connectionBuilder;
        this.startTimer();
    }
    public ClientDispatcher getClientDispatcher() {
        return this.clientDispatcher;
    }
    private void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                System.err.println("\ntimer's run out");
                SocketServer.this.connectionBuilder.connectionLost(SocketServer.this, false);
            }
        };
        timer.schedule(timerTask, 6000);
    }
    private void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
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
            System.out.println(e.getMessage() + " \n error while sending message to server");
        }
    }

    // LISTEN CALLS FROM SERVER
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (connectionOpen) {
            try {
                MessagesFromServer message = (MessagesFromServer) inputStream.readObject();
                executorService.submit(() -> this.clientDispatcher.registerMessageFromServer(message));
                stopTimer();
                startTimer();
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