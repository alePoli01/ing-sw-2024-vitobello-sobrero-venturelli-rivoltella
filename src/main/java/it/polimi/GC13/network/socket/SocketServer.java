package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class SocketServer implements ServerInterface, Runnable, Serializable {
    /*
    class that represents the "virtual server" for the client

    clients calls the methods of this class
    the class then creates the messages and sends them to the
    server where they'll be elaborated
     */
    private final ObjectInputStream inputStream;
    private final ObjectOutputStream outputStream;
    private final ClientDispatcherInterface clientDispatcher;

    public SocketServer(Socket socket, ClientDispatcher clientDispatcher) throws IOException {

        this.outputStream = new ObjectOutputStream(socket.getOutputStream());
        this.inputStream = new ObjectInputStream(socket.getInputStream());
        this.clientDispatcher = clientDispatcher;
    }

    @Override
    public synchronized void addPlayerToGame(String nickname, int numOfPlayers) throws NicknameAlreadyTakenException {
        PlayerJoiningMessage playerJoiningMessage = new PlayerJoiningMessage(nickname,numOfPlayers);
        try {
            outputStream.writeObject(playerJoiningMessage);
            outputStream.flush();
        } catch (IOException e) {
            /*
            TODO: use an update to notify client's listener instead of throwing exception
             */
            System.out.println("invio messaggio fallito");
            e.printStackTrace();
        }
    }

    @Override
    public void checkForExistingGame() {
        CheckForExistingGameMessage checkForExistingGame = new CheckForExistingGameMessage() ;
        try {
            outputStream.writeObject(checkForExistingGame);
            outputStream.flush();
        } catch (IOException e) {
            /*
            TODO: use an update to notify client's listener instead of throwing exception
             */
            System.out.println("invio messaggio fallito");
            e.printStackTrace();
        }
    }

    @Override
    public void quitGame() {

    }

    @Override
    public void writeMessage() {

    }

    @Override
    public void drawCard() {

    }

    // LISTEN CALLS FROM SERVER
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                MessagesFromServer message = (MessagesFromServer) inputStream.readObject();
                executorService.submit(() -> message.dispatch(clientDispatcher,this));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}