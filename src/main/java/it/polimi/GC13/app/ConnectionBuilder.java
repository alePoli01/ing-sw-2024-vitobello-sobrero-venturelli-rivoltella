package it.polimi.GC13.app;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.ReconnectPlayerToGameMessage;
import it.polimi.GC13.network.rmi.RMIConnectionAdapter;
import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.network.socket.SocketServer;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.TUI.TUI;
import it.polimi.GC13.view.View;

import java.io.IOException;
import java.net.Socket;
import java.rmi.RemoteException;
import java.util.Scanner;

public class ConnectionBuilder {
    private final int viewChoice;
    private final int connectionChoice;
    private int socketPort;
    private int RMIPort;

    public ConnectionBuilder(int viewChoice, int connectionChoice, int socketPort, int RMIPort) {
        this.viewChoice = viewChoice;
        this.connectionChoice = connectionChoice;
        this.socketPort = socketPort;
        this.RMIPort = RMIPort;
    }

    public View createView(View view) {
        switch (this.viewChoice) {
            case 0:
                view = new FrameManager();
                break;
            case 1:
                view = new TUI();
                break;
            default:
                System.err.println("Invalid view choice, how did this happen?");
                System.exit(-1);
                break;
        }
        return view;
    }

    public ServerInterface createServerConnection(ServerInterface virtualServer, ClientDispatcherInterface clientDispatcher) throws RemoteException {
        if (connectionChoice == 1) {
            // RMI SETUP
            RMIConnectionAdapter rmiConnectionAdapter = new RMIConnectionAdapter(clientDispatcher);
            virtualServer = rmiConnectionAdapter.startRMIConnection(System.getProperty("java.rmi.server.hostname"), this.RMIPort);
            System.out.println("Connection completed");
        } else {
            // SOCKET SETUP
            virtualServer = socketSetup(this.socketPort, clientDispatcher);
            if (virtualServer == null) System.exit(-1);
            new Thread((SocketServer) virtualServer).start();
        }
        return virtualServer;
    }

    public static ServerInterface socketSetup(int socketPort, ClientDispatcherInterface clientDispatcher) {
        try {
            Socket socket = new Socket(System.getProperty("java.rmi.server.hostname"), socketPort); // creating socket that represents the server
            return new SocketServer(socket, clientDispatcher); //the connection is socket so the virtual server is a SocketServer object
        } catch (IOException e) {
            System.err.println("Failed to create socket.");
            return null;
        }
    }
    private void connectionLost(ServerInterface virtualServer) {
        boolean connectionOpen=false;
        int attemptCount = 0;       //after tot attempts ask to keep trying
        int sleepTime = 1000;       //initial delay
        int maxTime = 20000;        //caps the sleepTime
        int totalElapsedTime = 0;   // to be deleted
        double backOffBase = 1.05;  //changes the exponential growth of th time

        System.out.println("Internet Connection Lost, trying to reconnect...");
        while (!connectionOpen) {
            try {
                Thread.sleep(sleepTime);
            } catch (InterruptedException ex) {
                throw new RuntimeException(ex);
            }
            try {
                if (virtualServer.getGameName() == null || virtualServer.getPlayerName() == null) {
                    System.err.println("GameName or PlayerName is Null");
                    System.exit(1);
                }
                virtualServer=this.createServerConnection(virtualServer,virtualServer.getClientDispatcher());
                // connectionOpen = true;
            } catch (IOException e) {
                //exponential backoff algorithm
                attemptCount++;
                totalElapsedTime += sleepTime;
                sleepTime = (int) Math.min(sleepTime * Math.pow(backOffBase, attemptCount), maxTime);
                System.out.println("Attempt " + attemptCount + " of " + sleepTime + "ms" + " totalElapsedTime=" + totalElapsedTime / 1000 + "s");
                if (attemptCount > 10) {
                    //after some attempts wait for user input
                    String answer;
                    Scanner scanner = new Scanner(System.in);
                    System.out.println("Still waiting for Internet Connection, do you want to keep trying? (y/n)");
                    do {
                        answer = scanner.nextLine();
                        if (answer.equalsIgnoreCase("y")) {
                            sleepTime = 2000;
                            attemptCount = 0;
                            System.out.println("trying to reconnect...");
                        } else if (answer.equalsIgnoreCase("n")) {
                            System.exit(0);
                        } else {
                            System.out.println("character not recognised, please choose (y/n)");
                        }
                    } while (!(answer.equals("y")) && !(answer.equals("n")));
                }
            }
        }
    }


}
