package it.polimi.GC13.app;

import it.polimi.GC13.controller.LobbyController;
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
        System.out.println("SocketAccepter running...");
        ServerSocket serverSocket = null;
        try {
            serverSocket = new ServerSocket(port);
            System.out.println("ServerSocket port: " + serverSocket.getLocalPort());
        } catch (IOException e) {
            System.err.println("Could not create ServerSocket on port " + port);
            System.out.println(e.getMessage());
            System.exit(-1);
        }
        while (true) {
            try {
                Socket socket = serverSocket.accept(); // waits for a client to connect
                System.out.println("Client connection accepted...");
                SocketClient socketClient = new SocketClient(socket, serverDispatcher);
                //the socketClient (it's on the server) will wait for incoming messages from the client
                new Thread(socketClient).start();
                System.out.println("\t\tCreating an Impulse generator");
                ServerImpulse serverImpulse = new ServerImpulse(socketClient);
                System.out.println("\t\tStarting the Impulse generator Thread");
                new Thread(serverImpulse).start();
                System.out.println("\t\tClient connection is done");
            } catch (IOException e) {
                System.err.println(e.getMessage());
            }
        }
    }
}
