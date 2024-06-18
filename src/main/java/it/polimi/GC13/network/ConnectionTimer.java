package it.polimi.GC13.network;

import it.polimi.GC13.app.ConnectionBuilder;

import java.util.Timer;
import java.util.TimerTask;

public class ConnectionTimer {
    private Timer timer;
    private final ServerInterface virtualServer;
    private final ConnectionBuilder connectionBuilder;

    public ConnectionTimer(ServerInterface virtualServer, ConnectionBuilder connectionBuilder) {
        this.virtualServer = virtualServer;
        this.connectionBuilder = connectionBuilder;
        startTimer();
    }

    public void startTimer() {
        this.timer = new Timer();
        TimerTask timerTask = new TimerTask() {
            public void run() {
                System.err.println("\nCONNECTION TIMER EXPIRED");
                virtualServer.setConnectionOpen(false);
                connectionBuilder.connectionLost(virtualServer, false);
            }
        };
        timer.schedule(timerTask, 6000);
    }

    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }
}
