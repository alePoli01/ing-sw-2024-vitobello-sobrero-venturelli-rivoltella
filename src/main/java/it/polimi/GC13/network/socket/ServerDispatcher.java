package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

public class ServerDispatcher implements ServerDispatcherInterface {

    @Override
    public void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface view) {
        //now the server knows what to do
    }
}
