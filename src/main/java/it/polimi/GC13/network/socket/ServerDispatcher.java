package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.*;

public class ServerDispatcher implements ServerDispatcherInterface {
    private final ControllerDispatcher controllerDispatcher;

    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void sendToControllerDispatcher(MessagesFromClient messagesFromClient, ClientInterface client) {
        this.controllerDispatcher.dispatchMessagesFromClient(client, messagesFromClient);
    }
}
