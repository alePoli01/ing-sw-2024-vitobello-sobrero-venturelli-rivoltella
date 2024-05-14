package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public class OnPlayerNotAddedMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final String playerNickname;

    public OnPlayerNotAddedMessage(String playerNickname, String gameName) {
        this.errorMessage = gameName + " already started.";
        this.playerNickname = playerNickname;
    }

    @Override
    public void methodToRecall(View TUI) {
        TUI.checkForExistingGame();
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public String getPlayerNickname() {
        return this.playerNickname;
    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }
}
