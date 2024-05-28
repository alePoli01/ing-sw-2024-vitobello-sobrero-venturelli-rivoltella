package it.polimi.GC13.network.messages.fromserver;


import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;
import java.rmi.RemoteException;
import java.util.List;

public record OnDealPrivateObjectiveCardsMessage(String playerNickname, List<Integer> privateObjectiveCards) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        try {
            view.choosePrivateObjectiveCard(this.playerNickname,this.privateObjectiveCards());
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
    }
}
