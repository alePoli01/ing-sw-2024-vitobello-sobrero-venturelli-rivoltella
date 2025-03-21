package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public record OnPlayerNotReconnectedMessage(String playerNickname) implements OnInputExceptionMessage {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }

    @Override
    public void methodToRecall(View view) {
        view.checkForExistingGame();
    }

    @Override
    public String getErrorMessage() {
        return "GameName or PlayerName not Found";
    }

    @Override
    public String getPlayerNickname() {
        return playerNickname;
    }
}
