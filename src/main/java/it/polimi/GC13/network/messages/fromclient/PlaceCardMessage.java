package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a message from a client to place a card on the game board.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record PlaceCardMessage(int serialCardToPlace, boolean isFlipped, int X, int Y) implements MessagesFromClient {

    /**
     * Executes the operation to place a card on the game board.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.placeCard(player, this.serialCardToPlace(), this.isFlipped(), this.X(), this.Y());
    }
}
