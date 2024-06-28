package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

/**
 * Represents a message from the client to add a player to a game.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record AddPlayerToGameMessage(String playerNickname, String gameName) implements MessagesFromClient {

    /**
     * Executes the operation to add a player to the game.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException {
        lobbyController.addPlayerToGame(client, this.playerNickname(), this.gameName());
    }
}
