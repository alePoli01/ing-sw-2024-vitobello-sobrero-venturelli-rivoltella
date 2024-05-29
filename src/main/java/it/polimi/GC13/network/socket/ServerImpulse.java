package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.PokeMessage;

import java.rmi.RemoteException;

public class ServerImpulse implements Runnable {
    private final ClientInterface virtualClient;
    private final int delay;

    public ServerImpulse(ClientInterface virtualClient) {
        this.virtualClient = virtualClient;
        this.delay = 2000; //ms
    }

    @Override
    public void run() {
        while (true) {
            try {
                this.virtualClient.sendMessageFromServer(new PokeMessage());
            if(!virtualClient.isConnectionOpen()){
                break;
            }
                Thread.sleep(delay);
            } catch (InterruptedException | RemoteException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
