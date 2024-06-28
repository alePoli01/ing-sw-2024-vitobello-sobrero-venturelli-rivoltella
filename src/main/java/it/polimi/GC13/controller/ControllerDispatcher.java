package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromclient.MessagesFromClient;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Class that receives all messages from {@link it.polimi.GC13.network.socket.ServerDispatcher} and it forwards them to the {@link Controller}
 */
public class ControllerDispatcher {
    /**
     * The {@link LobbyController} instance used for managing game and player interactions.
     */
    private final LobbyController lobbyController;

    /**
     * Mapping of clients to their respective game controllers.
     */
    private final Map<ClientInterface, Controller> clientControllerMap = new ConcurrentHashMap<>(); //links client to his game's controller

    /**
     * Mapping of clients to their respective players.
     */
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>(); //links the client to the player



    /**
     * Constructs a new {@code ControllerDispatcher} with the specified LobbyController.
     *
     * @param lobbyController The LobbyController instance to use for managing game and player interactions.
     */
    public ControllerDispatcher(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    /**
     * Retrieves the mapping of clients to their respective game controllers.
     *
     * @return A ConcurrentHashMap representing the mapping of clients to their game controllers.
     */
    public Map<ClientInterface, Controller> getClientControllerMap() {
        return clientControllerMap;
    }

    /**
     * Retrieves the mapping of clients to their respective players.
     *
     * @return A HashMap representing the mapping of clients to their players.
     */
    public Map<ClientInterface, Player> getClientPlayerMap() {
        return clientPlayerMap;
    }

    /**
     * Dispatches messages received from clients to the appropriate game controller and player.
     * Calls the corresponding method on the LobbyController to handle the message.
     *
     * @param client The client interface representing the client sending the message.
     * @param messagesFromClient The message received from the client.
     */
    public void dispatchMessagesFromClient(ClientInterface client, MessagesFromClient messagesFromClient) {
        try {
            messagesFromClient.methodToCall(this.lobbyController, this.clientControllerMap.get(client), client, this.clientPlayerMap.get(client));
        } catch (RemoteException e) {
            System.err.println("RMI: Error while dispatching messages from client: "+e.getLocalizedMessage());
        }
    }
}
