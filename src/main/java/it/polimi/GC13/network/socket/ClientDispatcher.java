package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.LostConnectionToServerInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.view.View;

import java.io.Serializable;
import java.util.LinkedList;

public class ClientDispatcher implements Serializable,ClientDispatcherInterface {
    private View view;
    private final LinkedList<MessagesFromServer> messageFromServerList = new LinkedList<>();

    public ClientDispatcher() {
    }

    public void setView(View view) {
        this.view = view;
    }

    private void callMessages() {
        do {
            this.messageFromServerList.removeFirst().methodToCall(view);
        } while (!this.messageFromServerList.isEmpty());
    }

    @Override
    public void registerMessageFromServer(MessagesFromServer messagesFromServer) {
        this.messageFromServerList.addLast(messagesFromServer);
        this.callMessages();
    }
    }
