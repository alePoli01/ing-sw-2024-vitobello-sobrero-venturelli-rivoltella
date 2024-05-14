package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public class OnNotEnoughResourceToPlaceMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final String playerNickname;

    public OnNotEnoughResourceToPlaceMessage(String playerNickname, Resource reign) {
        this.errorMessage = "You don't have enough" + reign + "to play this card";
        this.playerNickname = playerNickname;
    }

    @Override
    public void methodToRecall(View view) {
        view.showHomeMenu();
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
