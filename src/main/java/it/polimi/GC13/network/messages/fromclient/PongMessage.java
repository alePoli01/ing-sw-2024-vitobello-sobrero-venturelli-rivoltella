package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

/**
 * Represents a PONG message sent from a client to respond to a PING message from the server.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public class PongMessage implements MessagesFromClient {

    /**
     * Executes the operation to handle the PONG message from the client.     * @param lobbyController the lobby controller handling the client's request.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException {
        lobbyController.pongAnswer(client);
    }
}
