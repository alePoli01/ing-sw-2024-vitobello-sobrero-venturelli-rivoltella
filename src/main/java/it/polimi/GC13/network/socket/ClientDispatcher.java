package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.LostConnectionToServerInterface;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnExceptionMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;
import it.polimi.GC13.view.View;

import java.io.IOException;

public class ClientDispatcher implements ClientDispatcherInterface, LostConnectionToServerInterface {
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

    }

    @Override
    public void dispatch(OnCheckForExistingGameMessage onCheckForExistingGameMessage) {
        try {
            view.display(onCheckForExistingGameMessage.getWaitingPlayersMap(), onCheckForExistingGameMessage.getJoinableGameMap());
        } catch(IOException e) {
            System.out.println("Error dispatching game: " + e.getMessage());
        }
    }

    @Override
    public void dispatch(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage) {
        try {
            view.setupPhase(onPlayerAddedToGameMessage.getWaitingPlayers());
        } catch (IOException e) {
            System.out.println("Error adding players to game: " + e.getMessage());
        }
    }

    @Override
    public void dispatch(OnExceptionMessage onExceptionMessage) {
        view.printExceptionError(onExceptionMessage.getException());
    }

    @Override
    public void connectionLost(ServerInterface server) {
        System.out.println("****ERROR CONNECTION TO SERVER LOST****");
        System.out.println("da qua bisogna capire come ricollegarsi");
    }
}
