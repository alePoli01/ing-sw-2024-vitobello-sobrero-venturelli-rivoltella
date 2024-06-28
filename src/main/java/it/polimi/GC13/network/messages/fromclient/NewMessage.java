package it.polimi.GC13.network.messages.fromclient;


import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a new chat message sent from a client.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record NewMessage(String sender, String recipient, String message) implements MessagesFromClient {

    /**
     * Executes the operation to handle a new chat message.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        System.err.println("[NewMessage] " + sender + " to " + recipient + ": " + message);
        gamePhase.newChatMessage(this.sender(), this.recipient(), this.message());
    }
}
