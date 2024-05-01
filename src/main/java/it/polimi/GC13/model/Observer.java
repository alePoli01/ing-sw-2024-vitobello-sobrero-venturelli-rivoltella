package it.polimi.GC13.model;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;

import java.util.ArrayList;
import java.util.List;

public class Observer {
    private final List<ClientInterface> listenerList;

    public Observer() {
        this.listenerList = new ArrayList<>();
    }

    public void addListener(ClientInterface listener) {
        this.listenerList.add(listener);
    }

    public void notifyClients(MessagesFromServer message) {
        this.listenerList.forEach(message::notifyClient);
    }
}
