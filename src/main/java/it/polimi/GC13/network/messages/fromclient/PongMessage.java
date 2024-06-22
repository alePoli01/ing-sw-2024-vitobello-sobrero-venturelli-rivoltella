package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

public class PongMessage implements MessagesFromClient {
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException {
        lobbyController.pongAnswer(client);
    }
}
