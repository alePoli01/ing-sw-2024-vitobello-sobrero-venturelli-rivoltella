package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.LostConnectionToClientInterface;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.messages.fromserver.*;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient implements ClientInterface, Runnable {
    /*
   class that represents the "virtual client" for the server

   server calls the methods of this class
   the class then creates the messages and sends them to the
   client where they'll be elaborated
    */
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final ServerDispatcherInterface serverDispatcher;
    private final LostConnectionToClientInterface connectionStatus;
    private boolean connectionOpen = true;

    public SocketClient(Socket socket, ServerDispatcher serverDispatcher, LostConnectionToClientInterface connectionStatus) throws IOException {
        this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.serverDispatcher = serverDispatcher;
        this.connectionStatus = connectionStatus;
    }


    /*
        TODO: è possibile che andrà rivisto il nome e l'implementazione quando faremo RMI
        Generic method to send messages from server to client
     */
    @Override
    public void sendMessage(MessagesFromServer message) {
        try {
            if (!connectionOpen) {
                return;
            }
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            connectionOpen = false;
            System.out.println("Error sending message: " + e.getMessage());
            connectionStatus.connectionLost(this);
        }
    }

    @Override
    public void startRMIConnection() {

    }

    @Override
    public synchronized void poke() {
        //empty message sent by the Impulse (read comment in SocketAccepter)
        this.sendMessage(new PokeMessage());
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
                MessagesFromClient message = (MessagesFromClient) ois.readObject();
                executorService.submit(() -> {
                    try {
                        message.dispatch(serverDispatcher, this);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
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
