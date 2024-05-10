package it.polimi.GC13.network.socket.messages.fromclient;


import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

public record NewMessage(String sender, String receiver, String message) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.registerMessage(this.sender(), this.receiver(), this.message());
    }
}
