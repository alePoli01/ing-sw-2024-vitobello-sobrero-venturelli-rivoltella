package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.util.Arrays;
import java.util.List;

public record OnPlayerAddedToGameMessage(int connectedPlayers, int numPlayersNeeded) implements MessagesFromServer {

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        List<TokenColor> tokenColorList = Arrays.asList(TokenColor.values());
        view.chooseTokenSetupPhase(this.connectedPlayers, tokenColorList, this.numPlayersNeeded);
    }
}
