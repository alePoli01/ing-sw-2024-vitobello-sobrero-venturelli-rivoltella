package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.PokeMessage;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
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

    public SocketClient(Socket socket, ServerDispatcher serverDispatcher) throws IOException {
        this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.serverDispatcher = serverDispatcher;
    }

    // Generic method to send messages from server to client
    private void sendMessage(MessagesFromServer message) {
        try {
            oos.writeObject(message);
            oos.flush();
        } catch (IOException e) {
            System.out.println("Error sending message: " + e.getMessage());
        }
    }

    @Override
    public void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException {

    }

    @Override
    public void onCheckForExistingGame(int waitingPlayers) {
        //sent when responding to check for existing game message
        OnCheckForExistingGameMessage onCheckForExistingGameMessage = new OnCheckForExistingGameMessage(waitingPlayers);
        this.sendMessage(onCheckForExistingGameMessage);
    }

    @Override
    public void onPlayerAddedToGame(int waitingPlayers) {
        OnPlayerAddedToGameMessage onPlayerAddedToGameMessage = new OnPlayerAddedToGameMessage(waitingPlayers);
        this.sendMessage(onPlayerAddedToGameMessage);
    }

    @Override
    public synchronized void poke() throws IOException {
        //empty message sent by the Impulse (read comment in SocketAccepter)
        PokeMessage message = new PokeMessage("Try finger but hole");
        oos.writeObject(message);
        oos.flush();
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
            /*try {
                synchronized (this) {
                    if(!OPEN){
                        break;
                    }
                }*/
            try {
                MessagesFromClient message = (MessagesFromClient) ois.readObject();
                executorService.submit(()-> {
                    try {
                        System.out.println("ciao");
                        message.dispatch(serverDispatcher, this);
                    } catch (IOException | PlayerNotAddedException e) {
                        throw new RuntimeException(e);
                    } catch (NicknameAlreadyTakenException e) {

                    }
                });
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
            /*} catch (Exception e) {
                /*synchronized (this) {
                    if(OPEN) {
                        OPEN = false;
                        onConnectionLostListener.onConnectionLost(this);
                    }
                }*/
        }
    }
}
