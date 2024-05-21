package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.LostConnectionToClientInterface;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.*;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient implements ClientInterface, Runnable {
    /*
   class that represents the "virtual client" for the server

   server calls the methods of this class
   the class then creates the messages and sends them to the
   client where they'll be elaborated
    */
    private final ObjectOutputStream outputStream;
    private final ObjectInputStream inputStream;
    private final ServerDispatcherInterface serverDispatcher;
    private final LostConnectionToClientInterface connectionStatus;
    private boolean connectionOpen = true;

    public SocketClient(Socket socket, ServerDispatcher serverDispatcher, LostConnectionToClientInterface connectionStatus) throws IOException {
        this.outputStream = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.outputStream.flush();
        this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.serverDispatcher = serverDispatcher;
        this.connectionStatus = connectionStatus;
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
            connectionOpen = false;
            System.out.println("Error sending message: " + e.getMessage());
            connectionStatus.connectionLost(this);
        }
    }


    @Override
    public void run() {
        /*
        the methods above are for sending messages to the client
        chain of calls:
        1.(interface)MessageFromClients at runtime is one of the messages (example: MessageFromClient)
        2.MessageFromClient.dispatch calls the ServerDispatcher passing itself so that,based on the Class type, we know what to do
        3.ServerDispatcher.dispatch calls the ControllerDispatcher.specific_method
        4.ControllerDispatcher calls either the lobby or the controller
         */
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                if (!connectionOpen) break;
                MessagesFromClient message = (MessagesFromClient) inputStream.readObject();
                executorService.submit(() -> {
                    try {
                        serverDispatcher.sendToControllerDispatcher(message, this);
                    } catch (RemoteException e) {
                        System.err.println("Client disconnected\n " + e.getMessage());
                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                if (connectionOpen) {
                    connectionOpen = false;
                    connectionStatus.connectionLost(this);
                }
            }
        }
    }
}
