package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.io.Serializable;
import java.rmi.RemoteException;

/**
 * Interface representing messages sent from the client to the server.
 * Implementations of this interface define methods that the server can call to process client requests.
 */
public interface MessagesFromClient extends Serializable {

    /**
     * Method to be implemented by each message type.
     *
     * @param lobbyController the lobby controller handling the client's request.
     * @param gamePhase the current phase of the game when the message was received.
     * @param client the client interface through which the server communicates back with the client.
     * @param player the player associated with the client sending the message.
     * @throws RemoteException if there is an issue with remote method invocation.
     */
    void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) throws RemoteException;
}
