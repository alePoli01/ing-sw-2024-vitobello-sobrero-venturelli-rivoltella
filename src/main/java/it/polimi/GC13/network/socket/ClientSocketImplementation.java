package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.ServerMessage;

import java.io.*;
import java.net.Socket;
import java.util.concurrent.*;

public class ClientSocketImplementation implements Runnable, ClientInterface {
    /*
    class that represents the "virtual client" on the server

    controller calls the methods of this class giving
    updates to the view on client
     */

        private final ObjectInputStream inputStream;
        private final ObjectOutputStream outputStream;
        private final ServerDispatcherInterface serverDispatcher;

        public ClientSocketImplementation(Socket socket, ServerDispatcherInterface serverDispatcher) throws IOException {
            this.inputStream = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
            this.outputStream= new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
            this.serverDispatcher=serverDispatcher;
        }

    @Override
    public void run() {
        ExecutorService executorService = Executors.newCachedThreadPool();
        while (true) {
            try {
                /*
                1. message is referred to as (Interface)ServerMessage, but it's one of the classes
                    in (Package)messages (they all implement ServerMessage)
                2. calling message.dispatch(...) actually calls the dispatch method in the messages classes
                */
                ServerMessage message = (ServerMessage) inputStream.readObject();
                executorService.submit(() -> message.dispatch(serverDispatcher, this));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
