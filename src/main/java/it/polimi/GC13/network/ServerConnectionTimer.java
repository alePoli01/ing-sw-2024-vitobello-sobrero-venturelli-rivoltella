package it.polimi.GC13.network;

import it.polimi.GC13.controller.LobbyController;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Manages a timer for server-client connection validation.
 * <p>
 * This class creates and manages a timer that monitors the connection with a client
 * represented by {@link ClientInterface}. If the timer expires, it notifies the
 * {@link LobbyController} to close the game for that client.
 */
public class ServerConnectionTimer {

    /**
     * The timer instance used to schedule connection validation tasks.
     */
    private Timer timer;

    /**
     * The client interface associated with this timer for connection monitoring.
     */
    private final ClientInterface client;

    /**
     * The lobby controller responsible for managing game sessions and connections.
     */
    private final LobbyController lobbyController;

    /**
     * Flag indicating whether the timer is currently considered valid.
     * <p>
     * This flag determines whether the timer actions are still relevant and should be executed.
     */
    private boolean validTimer = true;


    /**
     * Constructs a {@code ServerConnectionTimer} with the specified client and lobby controller.
     * Starts the timer immediately upon creation.
     *
     * @param client          The client interface to monitor for connection.
     * @param lobbyController The controller managing the lobby and game.
     */
    public ServerConnectionTimer(ClientInterface client, LobbyController lobbyController) {
        this.client = client;
        this.lobbyController = lobbyController;
        startTimer();
    }


    /**
     * Starts the timer to monitor the client's connection.
     * If the timer expires, it notifies the lobby controller to close the game for the client.
     */
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

    /**
     * Stops the currently running timer.
     */
    public void stopTimer() {
        if (this.timer != null) {
            this.timer.cancel();
        }
    }

    /**
     * Invalidates the timer, preventing it from running further actions.
     * Stops the timer if it is currently active.
     */
    public void invalidateTimer() {
        stopTimer();
        this.validTimer = false;
    }
}
