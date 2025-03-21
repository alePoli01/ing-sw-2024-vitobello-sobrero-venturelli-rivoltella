package it.polimi.GC13.network.messages.fromserver;


import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;
import java.rmi.RemoteException;

public record OnChoosePrivateObjectiveCardMessage(String playerNickname, int serialPrivateObjectiveCard, int readyPlayers, int neededPlayers) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.setPrivateObjectiveCard(this.playerNickname(), this.serialPrivateObjectiveCard(), this.readyPlayers(), this.neededPlayers());
    }
}
