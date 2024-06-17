package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public class OnNickNameAlreadyTakenMessage implements OnInputExceptionMessage {
    private final String playerNickname;
    private final String errorMessage;

    public OnNickNameAlreadyTakenMessage(String nickname) {
        this.playerNickname = nickname;
        this.errorMessage = "Nickname " + nickname + " is already taken";
    }

    @Override
    public void methodToRecall(View view) {
        view.checkForExistingGame();
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
