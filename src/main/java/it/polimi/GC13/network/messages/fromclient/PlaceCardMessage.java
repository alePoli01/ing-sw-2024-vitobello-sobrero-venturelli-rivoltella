package it.polimi.GC13.network.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

public record PlaceCardMessage(int serialCardToPlace, boolean isFlipped, int X, int Y) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.placeCard(player, this.serialCardToPlace(), this.isFlipped(), this.X(), this.Y());
    }
}
