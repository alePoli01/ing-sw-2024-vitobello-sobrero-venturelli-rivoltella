package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.view.View;

import java.io.IOException;

public class ClientDispatcher implements ClientDispatcherInterface {
    View view;
    /*
    TODO: ho iniziato a scrivere la parte di ricezione messaggi dal server ma credo serva implementare degli observer da qualche parte sulla TUI
     */
    public ClientDispatcher() {
    }

    public void setView(View view) {
        this.view = view;
    }

    @Override
    public void dispatch(String message) {
       // System.out.println(message);
    }

    @Override
    public void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage, ServerInterface server) {
        //System.out.println("--Received: checkForExistingGame: " + onCheckForExistingGameMessage.getNoExistingGames());
        try {
            view.display(onCheckForExistingGameMessage, onCheckForExistingGameMessage.getNoExistingGames());
        } catch(IOException e) {
            System.out.println("Error dispatching game: " + e.getMessage());
        }
    }

    @Override
    public void dipatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage) {
        view.update(onPlayerAddedToGameMessage);
    }
}
