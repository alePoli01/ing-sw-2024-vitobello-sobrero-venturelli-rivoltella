package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

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
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }
}
