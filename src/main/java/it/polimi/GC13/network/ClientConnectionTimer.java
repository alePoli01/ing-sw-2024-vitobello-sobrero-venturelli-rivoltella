package it.polimi.GC13.network;

import it.polimi.GC13.app.ConnectionBuilder;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages a timer for monitoring client-server connection status.
 */
public class ClientConnectionTimer {
    /**
     * The timer instance used for scheduling connection status checks.
     */
    private Timer timer;

    /**
     * The virtual server interface representing the remote server connection.
     */
    private final ServerInterface virtualServer;

    /**
     * The connection builder used for reconnecting in case of connection loss.
     */
    private final ConnectionBuilder connectionBuilder;


    /**
     * Constructs a {@code ClientConnectionTimer} with the specified virtual server and connection builder.
     *
     * @param virtualServer    The virtual server interface representing the remote server.
     * @param connectionBuilder The connection builder for managing reconnections.
     */
    public ClientConnectionTimer(ServerInterface virtualServer, ConnectionBuilder connectionBuilder) {
        //builds a timer that can reconnect
        this.virtualServer = virtualServer;
        this.connectionBuilder = connectionBuilder;
        startTimer();
    }


    /**
     * Starts the timer to monitor the connection status.
     * Upon timeout, marks the virtual server's connection as closed and notifies the connection builder.
     */
    public void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                virtualServer.setConnectionOpen(false);
                connectionBuilder.connectionLost(virtualServer);
            }
        };
        timer.schedule(timerTask, 6000);
    }

    /**
     * Stops the running timer.
     */
    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

}
