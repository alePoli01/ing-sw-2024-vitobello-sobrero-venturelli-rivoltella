package it.polimi.GC13.model;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;

import java.rmi.RemoteException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Observer {
    private final List<ClientInterface> listenerList = new ArrayList<>();
    private final DiskManager diskManager;
    private final String gameName;

    public Observer(DiskManager diskManager, Game game) {
        this.diskManager = diskManager;
        this.diskManager.setGameManaged(game);
        this.gameName = game.getGameName();
    }
    public int getListenerSize() {
        return this.listenerList.size();
    }

    public void addListener(ClientInterface listener) {
        this.listenerList.add(listener);
        System.out.println("[Game: "+ gameName +"][Listeners size: " + this.listenerList.size()+"]");
    }
    public void removeListener(ClientInterface listener) {
        this.listenerList.remove(listener);
    }

    public void notifyClients(MessagesFromServer message) {
        this.listenerList.forEach(client -> {
                try {
                    message.notifyClient(client);
                } catch (RemoteException e) {
                    System.err.println("RMI: Error notifying client: " + client);
                }
        });
        this.diskManager.writeOnDisk();
    }

    public DiskManager getDiskManager() {
        return this.diskManager;
    }
}
