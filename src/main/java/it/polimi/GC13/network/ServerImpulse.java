package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromserver.PingMessage;

import java.rmi.RemoteException;

/**
 * Runnable task representing a server impulse to periodically send ping messages to a client.
 * <p>
 * This class implements {@link Runnable} and is used to maintain a connection with a client by sending periodic
 * {@link PingMessage}s. It runs in a loop until the client's connection is closed or an exception occurs.
 */
public class ServerImpulse implements Runnable {

    /**
     * The client interface to communicate with.
     * <p>
     * This field stores the reference to the {@link ClientInterface} that represents the client
     * connected to the server. It is used to send {@link PingMessage}s periodically to maintain
     * communication with the client.
     */
    private final ClientInterface client;

    /**
     * Delay in milliseconds between consecutive pings.
     * <p>
     * This field specifies the time interval between consecutive {@link PingMessage}s sent
     * to the client. It is set to a default value of 2000 milliseconds (2 seconds) in the
     * constructor {@link ServerImpulse#ServerImpulse(ClientInterface)}.
     */
    private final int delay;



    /**
     * Constructs a {@code ServerImpulse} with the specified client interface.
     *
     * @param client the client interface to communicate with
     */
    public ServerImpulse(ClientInterface client) {
        this.client = client;
        this.delay = 2000; //ms
    }

    /**
     * Executes the server impulse task.
     * <p>
     * This method sends {@link PingMessage}s to the client at regular intervals specified by the delay. It checks if
     * the client's connection is open and sleeps for the specified delay period. The task continues running until the
     * client's connection is closed or an interruption occurs.
     */
    @Override
    public void run() {
        boolean running = true;
        while (running) {
            try {
                //send ping
                this.client.sendMessageFromServer(new PingMessage());
            if(!client.isConnectionOpen()){
                break;
            }
                Thread.sleep(delay);
            } catch (InterruptedException | RemoteException e){
                running = false;
            }
        }
    }
}
