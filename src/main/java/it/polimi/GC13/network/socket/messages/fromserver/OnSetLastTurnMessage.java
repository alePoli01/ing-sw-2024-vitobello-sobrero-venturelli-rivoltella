package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;

public record OnSetLastTurnMessage(String playerNickname, Position position) implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client)throws RemoteException {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.onSetLastTurn(this.playerNickname(), this.position());
    }
}
