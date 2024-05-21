package it.polimi.GC13.model;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Observer {
    private final List<ClientInterface> listenerList = new ArrayList<>();
    private final DiskManager diskManager;

    public Observer(DiskManager diskManager) {
        this.diskManager = diskManager;
    }

    public int getListenerSize() {
        return this.listenerList.size();
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
        this.diskManager.writeOnDisk();
    }
}
