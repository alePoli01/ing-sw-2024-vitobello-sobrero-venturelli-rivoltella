package it.polimi.GC13.network.socket;

import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.ClientMessage;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

/**
 *
 * DEPRECATO
 *
 *
 *
 *
 *
 */
public class SocketServer implements ServerInterface,  Runnable {
    /*
    class that represents the "virtual server" for the client

    clients calls the methods of this class
    the class then creates the messages and sends them to the
    server where they'll be elaborated
     */
    private ObjectInputStream inputStream;
    private ObjectOutputStream outputStream;

    public SocketServer(Socket socket) throws IOException {
        this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.outputStream= new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
    }

    @Override
    public void addPlayerToGame(Player player) {
        PlayerJoiningMessage playerJoiningMessage=new PlayerJoiningMessage(player);
        try {
            outputStream.writeObject(playerJoiningMessage);
            outputStream.flush();
        } catch (IOException e) {
            /*
            TODO: use an update to notify client's listener instead of throwing exception
             */
            throw new RuntimeException(e);
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

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while(true){
            try{
                ClientMessage message=(ClientMessage) inputStream.readObject();//reads the incoming message(including object type and parameters)
                /* clientDispatcher */
                executorService.submit(message::dispatch);
            }catch(Exception e){
                  e.printStackTrace();
            }
        }
    }
}
