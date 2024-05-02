package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.LostConnectionToServerInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.*;
import it.polimi.GC13.view.View;

import java.util.LinkedList;
import java.util.List;

public class ClientDispatcher implements ClientDispatcherInterface, LostConnectionToServerInterface {
    private View view;
    private final List<MessagesFromServer> serverMessages = new LinkedList<>();

    public ClientDispatcher() {
    }

    public void setView(View view) {
        this.view = view;
    }

    private void callMessages() {
        do {
            this.serverMessages.removeFirst().methodToCall(view);
        } while (this.serverMessages.isEmpty());
    }

    @Override
    public void registerFromServerMessage(MessagesFromServer message) {
        this.serverMessages.add(message);
        this.callMessages();
    }

    @Override
    public void dispatch(String message) {

    }

    @Override
    public void connectionLost(ServerInterface server) {
        System.out.println("****ERROR CONNECTION TO SERVER LOST****");
        System.out.println("da qua bisogna capire come ricollegarsi");
    }
}
