package it.polimi.GC13.controller;

import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.MessagesFromClient;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ControllerDispatcher {
    private final LobbyController lobbyController;
    private final Map<ClientInterface, Controller> clientControllerMap = new ConcurrentHashMap<>();
    private final Map<ClientInterface, Player> clientPlayerMap = new HashMap<>();

    public ControllerDispatcher(LobbyController lobbyController){
        this.lobbyController = lobbyController;
    }

    public Map<ClientInterface, Controller> getClientControllerMap() {
        return clientControllerMap;
    }

    public Map<ClientInterface, Player> getClientPlayerMap() {
        return clientPlayerMap;
    }

    public void dispatchMessagesFromClient(ClientInterface client, MessagesFromClient messagesFromClient) {
        this.clientPlayerMap.compute(client, (key, value) -> {
            try {
                messagesFromClient.methodToCall(this.lobbyController, this.clientControllerMap.get(client), client, this.clientPlayerMap.get(client));
            } catch (RemoteException e) {
                System.err.println("RMI: Error while dispatching messages from client.");
                e.printStackTrace();
            }
            return value;
        });
    }
}
