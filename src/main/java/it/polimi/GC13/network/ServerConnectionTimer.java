package it.polimi.GC13.network;

import it.polimi.GC13.controller.LobbyController;

import java.util.Timer;
import java.util.TimerTask;

public class ServerConnectionTimer {
    private Timer timer;
    private final ClientInterface client;
    private final LobbyController lobbyController;
    private boolean validTimer = true;

    public ServerConnectionTimer(ClientInterface client, LobbyController lobbyController) {
        this.client = client;
        this.lobbyController = lobbyController;
        startTimer();
    }

    public void startTimer() {
        if(this.validTimer) {
            this.timer = new Timer();
            TimerTask timerTask = new TimerTask() {
                public void run() {
                    if(validTimer){
                        System.err.println("\nCONNECTION TIMER EXPIRED");
                        lobbyController.closeGame(client);
                    }
                }
            };
            this.timer.schedule(timerTask, 6000);
        }
    }
    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }
    public void invalidateTimer() {
        stopTimer();
        this.validTimer = false;
    }
}
