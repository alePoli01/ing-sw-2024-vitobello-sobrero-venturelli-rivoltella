package it.polimi.GC13.network.socket;

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
                    Socket socket = serverSocket.accept();
                    socket.setSoTimeout(10000);
                    System.out.println("Client connection accepted...");

                    SocketClient socketClient = new SocketClient(socket, serverDispatcher);

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
