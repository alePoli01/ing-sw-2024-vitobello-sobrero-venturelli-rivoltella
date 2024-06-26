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
    private final LobbyController lobbyController;
    private final Map<ClientInterface, Controller> clientControllerMap = new ConcurrentHashMap<>(); //links client to his game's controller
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>(); //links the client to the player

    public ControllerDispatcher(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    public Map<ClientInterface, Controller> getClientControllerMap() {
       // DEBUG
       /*  clientControllerMap.forEach((key, value) -> {
            System.out.print(key.hashCode()+", ");
            System.out.println(value.getGameController().getClass().getSimpleName());
        });*/
        return clientControllerMap;
    }

    public Map<ClientInterface, Player> getClientPlayerMap() {
        return clientPlayerMap;
    }

    public void dispatchMessagesFromClient(ClientInterface client, MessagesFromClient messagesFromClient) {
        try {
            messagesFromClient.methodToCall(this.lobbyController, this.clientControllerMap.get(client), client, this.clientPlayerMap.get(client));
        } catch (RemoteException e) {
            System.err.println("RMI: Error while dispatching messages from client: "+e.getLocalizedMessage());
        }
    }
}
