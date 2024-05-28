package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public record OnPlayerAddedToGameMessage(int connectedPlayers, int numPlayersNeeded, String gameName) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        List<TokenColor> tokenColorList = Arrays.asList(TokenColor.values());
        try {
            view.chooseTokenSetupPhase(this.connectedPlayers, this.numPlayersNeeded, tokenColorList, this.gameName());
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }
}
