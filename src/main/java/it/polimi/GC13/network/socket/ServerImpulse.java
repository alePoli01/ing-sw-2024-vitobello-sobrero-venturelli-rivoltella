package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.socket.messages.fromserver.PokeMessage;

public class ServerImpulse implements Runnable {
    private final SocketClient socketClient;
    private final int delay;

    public ServerImpulse(SocketClient socketClient) {
        this.socketClient = socketClient;
        this.delay = 10000;
    }

    @Override
    public void run() {
        while (true) {
            this.socketClient.sendMessage(new PokeMessage());
            try {
                Thread.sleep(delay);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
