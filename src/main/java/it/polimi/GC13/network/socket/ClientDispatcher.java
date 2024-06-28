package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.view.View;

import java.util.concurrent.BlockingDeque;
import java.util.concurrent.LinkedBlockingDeque;

/**
 * ClientDispatcher class that manages messages received from the server and delegates them to the associated {@link View}.
 */
public class ClientDispatcher {
    /**
     * The view associated with this dispatcher, where messages will be processed.
     */
    private View view;

    /**
     * Queue to store messages received from the server.
     */
    private final BlockingDeque<MessagesFromServer> messageFromServerList = new LinkedBlockingDeque<>();


    /**
     * Sets the view associated with this dispatcher.
     *
     * @param view The view to associate with this dispatcher.
     */
    public void setView(View view) {
        this.view = view;
    }


    /**
     * Processes all messages currently in the queue by calling their corresponding methods on the associated view.
     * This method is called after receiving a new message and ensures all pending messages are executed in order.
     */
    private void callMessages() {
        do {
            try {
                this.messageFromServerList.takeFirst().methodToCall(view);
            } catch (InterruptedException e) {
                System.err.println("Interrupted while executing messages from server");
            }
        } while (!this.messageFromServerList.isEmpty());
    }


    /**
     * Registers a message received from the server into the message queue and processes it immediately.
     *
     * @param messagesFromServer The message received from the server to register and process.
     */
    public void registerMessageFromServer(MessagesFromServer messagesFromServer) {
        try {
            this.messageFromServerList.put(messagesFromServer);
        } catch (InterruptedException e) {
            System.err.println("Interrupted while saving messages from server");
        }
        this.callMessages();
    }
}
