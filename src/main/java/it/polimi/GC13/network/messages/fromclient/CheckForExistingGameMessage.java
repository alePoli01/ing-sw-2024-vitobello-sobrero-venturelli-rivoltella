package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

/**
 * Represents a message from the client to check for an existing game.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record CheckForExistingGameMessage() implements MessagesFromClient {

    /**
     * Executes the operation to check for an existing game.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException {
        lobbyController.checkForExistingGame(client);
    }
}
