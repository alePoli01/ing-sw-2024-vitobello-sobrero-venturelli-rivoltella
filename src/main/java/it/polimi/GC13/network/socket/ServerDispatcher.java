package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

/**
 * Class that represents ServerDispatcher which receives all messages from the {@link SocketClient} and send them to the {@link ControllerDispatcher}
 */
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
