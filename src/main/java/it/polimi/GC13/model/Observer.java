package it.polimi.GC13.model;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.io.Serializable;
import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;

public class Observer implements Serializable {
    private transient List<ClientInterface> listenerList = new ArrayList<>();
    private final DiskManager diskManager;

    public Observer(DiskManager diskManager, Game game) {
        this.diskManager = diskManager;
        this.diskManager.setGameManaged(game);
    }

    public int getListenerSize() {
        return this.listenerList.size();
    }

    public void addListener(ClientInterface listener) {
        this.listenerList.add(listener);
    }

    public void rebuildClientList(List<ClientInterface> listenerList) {
        this.listenerList.addAll(listenerList);
        listenerList.forEach(client -> System.out.println(client + " added"));
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

    public DiskManager getDiskManager() {
        return this.diskManager;
    }
}
