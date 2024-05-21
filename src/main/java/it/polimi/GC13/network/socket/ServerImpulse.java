package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.messages.fromserver.PokeMessage;

public class ServerImpulse implements Runnable {
    private final SocketClient socketClient;
    private final int delay;

    public ServerImpulse(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.delay = 1000;//ms
    }

    @Override
    public void run() {
        while (true) {
            this.socketClient.sendMessageFromServer(new PokeMessage());
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
