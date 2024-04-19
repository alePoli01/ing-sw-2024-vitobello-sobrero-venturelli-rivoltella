package it.polimi.GC13.app;

import it.polimi.GC13.network.socket.ServerDispatcher;
import it.polimi.GC13.network.socket.SocketClient;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;

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
                    socket.setSoTimeout(10000);
                    System.out.println("Client connection accepted...");

                    SocketClient socketClient = new SocketClient(socket, serverDispatcher);
                    //the socketClient (it's on the server) will wait for incoming messages from the client
                    new Thread(socketClient).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
