package it.polimi.GC13.network.socket;

import it.polimi.GC13.controller.ControllerDispatcher;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

/**
 * Class that represents ServerDispatcher which receives all messages from the {@link SocketClient} and send them to the {@link ControllerDispatcher}
 */
public class ServerDispatcher implements ServerDispatcherInterface {
    /**
     * The controller dispatcher responsible for handling messages from clients.
     */
    private final ControllerDispatcher controllerDispatcher;


    /**
     * Constructs a {@code ServerDispatcher} with the specified ControllerDispatcher.
     *
     * @param controllerDispatcher The controller dispatcher responsible for handling client messages.
     */
    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher = controllerDispatcher;
    }

    @Override
    public void sendToControllerDispatcher(MessagesFromClient messagesFromClient, ClientInterface client) {
        this.controllerDispatcher.dispatchMessagesFromClient(client, messagesFromClient);
    }
}
