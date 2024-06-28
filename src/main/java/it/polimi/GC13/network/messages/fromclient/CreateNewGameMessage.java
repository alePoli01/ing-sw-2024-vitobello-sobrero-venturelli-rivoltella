package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

/**
 * Represents a message from the client to create a new game.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record CreateNewGameMessage(String playerNickname, int numOfPlayers, String gameName) implements MessagesFromClient {

    /**
     * Executes the operation to create a new game with specified parameters.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player)throws RemoteException {
        lobbyController.createNewGame(client, this.playerNickname(), this.numOfPlayers(), this.gameName());
    }
}
