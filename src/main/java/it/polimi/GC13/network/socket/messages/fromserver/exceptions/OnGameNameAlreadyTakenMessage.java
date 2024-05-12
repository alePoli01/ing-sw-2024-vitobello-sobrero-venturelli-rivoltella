package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public class OnGameNameAlreadyTakenMessage implements OnInputExceptionMessage {
    private final String playerNickname;
    private final String errorMessage;

    public OnGameNameAlreadyTakenMessage(String nickname, String gameName) {
        this.playerNickname = nickname;
        this.errorMessage = "Game's name " + gameName + " is already taken. Please choose another one.";
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
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }
}