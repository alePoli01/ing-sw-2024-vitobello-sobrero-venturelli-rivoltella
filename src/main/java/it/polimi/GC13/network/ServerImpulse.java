package it.polimi.GC13.network;

import it.polimi.GC13.network.messages.fromserver.PingMessage;

import java.rmi.RemoteException;

public class ServerImpulse implements Runnable {
    private final ClientInterface client;
    private final int delay;

    public ServerImpulse(ClientInterface client) {
        this.client = client;
        this.delay = 2000; //ms
    }

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
