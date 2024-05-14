package it.polimi.GC13.network.socket;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.LostConnectionToServerInterface;
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

    public SocketServer(Socket socket, ClientDispatcher clientDispatcher) throws IOException {
        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
    }

    /*
        TODO gestione eccezioni in modo intelligiente: creare metodo eccezioni nella tui
     */

    @Override
    public void sendMessageFromClient(MessagesFromClient messages) {
        try {
            if (!connectionOpen) {
                return;
            }
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
            connectionOpen = false;
            System.out.println(e.getMessage() + "errore nel mandare messaggio al server");
            //connectionStatus.connectionLost(this);
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
                    this.connectionOpen = false;
                    //this.connectionStatus.connectionLost(this);
                }
            }
        }
        executorService.shutdown();
    }
}