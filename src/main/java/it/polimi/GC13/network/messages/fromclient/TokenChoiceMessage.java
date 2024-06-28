package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a message from a client indicating a choice of token color.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record TokenChoiceMessage(TokenColor tokenColor) implements MessagesFromClient {

    /**
     * Executes the operation to choose a token color for a player.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.chooseToken(player, this.tokenColor());
    }
}
