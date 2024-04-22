package it.polimi.GC13.app;

import it.polimi.GC13.network.socket.ServerDispatcher;
import it.polimi.GC13.network.socket.ServerImpulse;
import it.polimi.GC13.network.socket.SocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class SocketAccepter implements Runnable {

    private final ServerDispatcher serverDispatcher;

    private final int port;

    public SocketAccepter(ServerDispatcher serverDispatcher, int port) {
        this.port = port;
        this.serverDispatcher = serverDispatcher;
    }

    @Override
    public void run() {
        try {
            System.out.println("SocketAccepter running...");
            ServerSocket serverSocket = new ServerSocket(port);
            while (true) {
                try {
                    Socket socket = serverSocket.accept(); // waits for a client to connect
                    System.out.println("\t\tClient connection accepted...");
                    SocketClient socketClient = new SocketClient(socket, serverDispatcher);
                    //the socketClient (it's on the server) will wait for incoming messages from the client
                    new Thread(socketClient).start();
                    /*
                    TODO: L'impulso potrebbe servire per capire quando la connessione viene persa, ma è da discutere
                    edit: se non c'è l'impulso non va niente
                     */
                    System.out.println("\t\tCreating an Impulse generator");
                    ServerImpulse serverImpulse= new ServerImpulse(socketClient);
                    System.out.println("\t\tStarting the Impulse generator Thread");
                    new Thread(serverImpulse).start();
                    System.out.println("\t\tClient connection is done\nSocketAccepter running...");
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
