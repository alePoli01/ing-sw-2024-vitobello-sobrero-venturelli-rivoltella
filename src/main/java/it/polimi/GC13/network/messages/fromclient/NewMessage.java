package it.polimi.GC13.network.messages.fromclient;


import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

public record NewMessage(String sender, String recipient, String message) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        System.err.println("[NewMessage] " + sender + " to " + recipient + ": " + message);
        gamePhase.newChatMessage(this.sender(), this.recipient(), this.message());
    }
}
