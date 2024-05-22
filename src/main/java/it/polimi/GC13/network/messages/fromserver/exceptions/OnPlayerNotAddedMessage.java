package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public record OnPlayerNotAddedMessage(String gameName, String playerNickname) implements OnInputExceptionMessage {

    @Override
    public void methodToRecall(View TUI) {
        TUI.checkForExistingGame();
    }

    @Override
    public String getErrorMessage() {
        return this.gameName() + " already started.";
    }

    @Override
    public String getPlayerNickname() {
        return this.playerNickname();
    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname(), this);
    }
}
