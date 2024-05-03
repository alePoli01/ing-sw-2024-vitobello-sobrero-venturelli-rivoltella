package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

public record PlaceCardMessage(int cardToPlaceHandIndex, boolean isFlipped, Coordinates xy) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.placeCard(player, this.cardToPlaceHandIndex(), this.isFlipped(), this.xy());
    }
}
