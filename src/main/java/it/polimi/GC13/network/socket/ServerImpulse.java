package it.polimi.GC13.network.socket;

import java.io.IOException;

public class ServerImpulse implements Runnable {
    private final SocketClient socketClient;
    private final int delay;
    public ServerImpulse(SocketClient socketClient) {
        this.socketClient=socketClient;
        this.delay=1000;


    }

    @Override
    public void run() {
        while (true) {
            socketClient.poke();
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
