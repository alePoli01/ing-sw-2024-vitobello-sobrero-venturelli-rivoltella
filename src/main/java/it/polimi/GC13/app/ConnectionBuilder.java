package it.polimi.GC13.app;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.rmi.RMIConnectionAdapter;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.GUI.game.FrameManager;
import it.polimi.GC13.view.TUI.TUI;
import it.polimi.GC13.view.View;

import java.io.IOException;
import java.net.Socket;

public class ConnectionBuilder {
    private final int viewChoice;
    private final int connectionChoice;
    private final int socketPort;
    private final int RMIPort;
    private View view;

    public ConnectionBuilder(int viewChoice, int connectionChoice, int socketPort, int RMIPort) {
        this.viewChoice = viewChoice;
        this.connectionChoice = connectionChoice;
        this.socketPort = socketPort;
        this.RMIPort = RMIPort;
    }

    public View createView() {
        switch (this.viewChoice) {
            case 2:
                this.view = new FrameManager();
                break;
            case 1:
                this.view = new TUI();
                break;
            default:
                System.err.println("Invalid view choice, how did this happen?");
                System.exit(-1);
                break;
        }
        return view;
    }

    public ServerInterface createServerConnection(ClientDispatcher clientDispatcher) throws IOException {
        if (connectionChoice == 1) {
            // RMI SETUP
            return rmiSetup(this.RMIPort, clientDispatcher);
        } else {
            // SOCKET SETUP
            ServerInterface serverInterface = socketSetup(this.socketPort, clientDispatcher);
            //thread to read incoming messages
            new Thread((SocketServer) serverInterface).start();

            return serverInterface;
        }
    }

    public ServerInterface rmiSetup(int RMIPort, ClientDispatcher clientDispatcher) throws IOException {
        //setup rmiAdapter
        RMIConnectionAdapter rmiConnectionAdapter = new RMIConnectionAdapter(clientDispatcher);

        return rmiConnectionAdapter.startRMIConnection(System.getProperty("java.server.hostname"), RMIPort,this);
    }

    public ServerInterface socketSetup(int socketPort, ClientDispatcher clientDispatcher) throws IOException {
        // creating socket that represents the server
        Socket socket;
        try {
            socket = new Socket(System.getProperty("java.server.hostname"), socketPort);
        } catch (IOException e) {
            System.err.println("Connection to server's socket failed");
            throw new IOException();
        }
        // the connection is socket so the virtual server is a SocketServer object
        return new SocketServer(socket, clientDispatcher, this);
    }

    /**
     * this method is used to remap the new virtual server. if the virtual server is different, the
     * @param virtualServer old virtual server
     */
    public synchronized void connectionLost(ServerInterface virtualServer) {
        this.view.restartConnection(virtualServer, this);
    }
}
