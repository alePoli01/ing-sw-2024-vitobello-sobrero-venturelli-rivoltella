package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a message from a client to place the starting card on the game board.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record PlaceStartCardMessage(boolean isFlipped) implements MessagesFromClient {

    /**
     * Executes the operation to place the starter card on the game board.
    */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.placeStartCard(player, this.isFlipped());
    }
}
