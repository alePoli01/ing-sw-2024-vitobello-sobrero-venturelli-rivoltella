package it.polimi.GC13.network.messages.fromserver;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

public record PingMessage() implements MessagesFromServer {

    @Override
    public void notifyClient(ClientInterface client) {

    }

    @Override
    public void methodToCall(View view) {

    }


}
