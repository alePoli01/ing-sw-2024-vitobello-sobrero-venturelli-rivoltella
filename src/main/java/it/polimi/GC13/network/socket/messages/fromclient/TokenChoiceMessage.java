package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;
import java.io.IOException;

public record TokenChoiceMessage(TokenColor tokenColor) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player) {
        gamePhase.chooseToken(player, this.tokenColor());
    }
}
