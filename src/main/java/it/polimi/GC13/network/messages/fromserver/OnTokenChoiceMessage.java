package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public record OnTokenChoiceMessage(String playerNickname, TokenColor tokenColor) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client)throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        try {
            view.placeStartCardSetupPhase(this.playerNickname, this.tokenColor);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }
}
