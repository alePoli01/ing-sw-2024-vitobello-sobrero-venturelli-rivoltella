package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

/**
 * Represents a message from a client requesting to reconnect a player to a game.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record ReconnectPlayerToGameMessage(String gameName,String playerName)implements MessagesFromClient {

    /**
     * Executes the operation to reconnect a player to a game.
    */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException {
        lobbyController.reconnectPlayerToGame(client, this.gameName,this.playerName);
    }
}
