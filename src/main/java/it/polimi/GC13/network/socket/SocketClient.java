package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.MessagesFromClient;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SocketClient implements ClientInterface, Runnable {
    /*
   class that represents the "virtual client" for the server

   clients calls the methods of this class
   the class then creates the messages and sends them to the
   server where they'll be elaborated
    */
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final ServerDispatcherInterface serverDispatcher;

    public SocketClient(Socket socket, ServerDispatcher serverDispatcher) throws IOException {
        this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.serverDispatcher = serverDispatcher;
    }



    @Override
    public void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException {

    }

    @Override
    public void run() {
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
