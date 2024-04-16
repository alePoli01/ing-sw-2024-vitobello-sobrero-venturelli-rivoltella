package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.model.Server;
import it.polimi.GC13.network.ClientInterface;

import java.io.*;
import java.net.Socket;
import java.rmi.NotBoundException;

public class SocketClient implements ClientInterface, Runnable {
    private final ObjectOutputStream oos;
    private final ObjectInputStream ois;
    private final ServerDispatcherInterface serverDispatcher;

    public SocketClient(Socket socket, ServerDispatcher serverDispatcher) throws IOException {
        this.oos = new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
        this.ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
        this.serverDispatcher = serverDispatcher;
    }

    @Override
    public Player getPlayer() {
        return null;
    }

    @Override
    public void startRMIConnection() throws IOException, NotBoundException, PlayerNotAddedException {

    }

    @Override
    public void run() {
        /*ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                synchronized (this) {
                    if(!OPEN){
                        break;
                    }
                }
                ServerMessage message = (ServerMessage) ois.readObject();
                executorService.submit(()->message.dispatch(serverDispatcher, this));
            } catch (Exception e) {
                synchronized (this) {
                    if(OPEN) {
                        OPEN = false;
                        onConnectionLostListener.onConnectionLost(this);
                    }
                }
            }
        }*/
    }
}
