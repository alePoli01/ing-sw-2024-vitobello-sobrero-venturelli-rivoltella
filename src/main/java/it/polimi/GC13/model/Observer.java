package it.polimi.GC13.model;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.Observable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Observer {

    public final List<ClientInterface> listenerList;
    private final ExecutorService executorService = Executors.newCachedThreadPool();

    public Observer() {
        this.listenerList = new ArrayList<>();
    }

    public void addListener(ClientInterface listener) {
        this.listenerList.add(listener);
    }

    public void notifyClients(MessagesFromServer message) {

        this.listenerList.forEach(client -> {

                try {
                    message.notifyClient(client);
                } catch (RemoteException e) {
                    System.err.println("RMI: Error notifying client: " + client);
                    e.printStackTrace();
                }
         });
    }
}
