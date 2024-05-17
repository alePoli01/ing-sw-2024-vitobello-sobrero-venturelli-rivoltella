package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.view.View;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

public class ClientDispatcher implements ClientDispatcherInterface {
    private View view;
    private final BlockingDeque<MessagesFromServer> messageFromServerList = new LinkedBlockingDeque<>();

    public void setView(View view) {
        this.view = view;
    }

    private void callMessages() {
        do {
            try {
                this.messageFromServerList.takeFirst().methodToCall(view);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while executing messages from server");
            }
        } while (!this.messageFromServerList.isEmpty());
    }

    @Override
    public void registerMessageFromServer(MessagesFromServer messagesFromServer) {
        try {
            this.messageFromServerList.put(messagesFromServer);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while saving messages from server");
        }
        this.callMessages();
    }
}
