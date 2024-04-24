package it.polimi.GC13.network.socket;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromclient.MessagesFromClient;
import it.polimi.GC13.network.socket.messages.fromclient.TokenChoiceMessage;
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

    /*
        TODO gestione eccezioni in modo intelligiente: creare metodo eccezioni nella tui
     */

    private void sendMessage(MessagesFromClient messages) {
        try {
            outputStream.writeObject(messages);
            outputStream.flush();
        } catch (IOException e) {
        }
    }

    @Override
    public synchronized void addPlayerToGame(String nickname, int numOfPlayers) {
        PlayerJoiningMessage playerJoiningMessage = new PlayerJoiningMessage(nickname,numOfPlayers);
        this.sendMessage(playerJoiningMessage);
    }

    @Override
    public void checkForExistingGame() {
        CheckForExistingGameMessage checkForExistingGame = new CheckForExistingGameMessage() ;
        this.sendMessage(checkForExistingGame);
    }

    @Override
    public void chooseToken(TokenColor tokenColor) {
        TokenChoiceMessage tokenChoiceMessage = new TokenChoiceMessage(tokenColor);
        this.sendMessage(tokenChoiceMessage);
    }

    @Override
    public void placeStartCard(boolean side) {

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
                executorService.submit(() -> message.dispatch(clientDispatcher));
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
    }
}