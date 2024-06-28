package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a message from the client to draw a card from the deck.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record DrawCardFromDeckMessage(int serialCardToDraw) implements MessagesFromClient {

    /**
     * Executes the operation to draw a card from the deck for the player.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.drawCard(player, this.serialCardToDraw());
    }
}
