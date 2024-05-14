package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;
import java.rmi.RemoteException;
public record OnPlaceCardMessage(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn) implements MessagesFromServer {


    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.onPlacedCard(this.playerNickname(), this.serialCardPlaced(), this.isFlipped(), this.x(), this.y() , this.turn());
    }
}