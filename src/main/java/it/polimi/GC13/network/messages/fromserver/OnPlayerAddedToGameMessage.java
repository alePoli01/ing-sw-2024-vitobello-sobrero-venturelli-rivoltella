package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.Arrays;
import java.util.List;

public record OnPlayerAddedToGameMessage(int connectedPlayers, int numPlayersNeeded) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        List<TokenColor> tokenColorList = Arrays.asList(TokenColor.values());
        view.chooseTokenSetupPhase(this.connectedPlayers, this.numPlayersNeeded, tokenColorList);
    }
}
