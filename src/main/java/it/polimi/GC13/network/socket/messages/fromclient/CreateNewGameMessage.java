package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.rmi.RemoteException;

//  implements ServerMessage because the interface has the dispatch method

public record CreateNewGameMessage(String playerNickname, int numOfPlayers, String gameName) implements MessagesFromClient {

    @Override
    public void methodToCall(LobbyController lobbyController, GamePhase gamePhase, ClientInterface client, Player player)throws RemoteException {
        lobbyController.createNewGame(client, this.playerNickname(), this.numOfPlayers(), this.gameName());
    }
}
