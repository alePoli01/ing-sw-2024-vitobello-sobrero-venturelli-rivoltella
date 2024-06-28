package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

/**
 * Represents a message from the client to choose a private objective card.
 * Implements {@link MessagesFromClient} interface, defining a method for server-side processing.
 */
public record ChoosePrivateObjectiveCardMessage(int serialPrivateObjectiveCard) implements MessagesFromClient {

    /**
     * Executes the operation to choose a private objective card for the player.
     */
    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player){
        gamePhase.choosePrivateObjective(player, this.serialPrivateObjectiveCard());
    }
}
