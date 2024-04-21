package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerJoiningMessage;

public class ClientDispatcher implements ClientDispatcherInterface{
    /*
    TODO: ho iniziato a scrivere la parte di ricezione messaggi dal server ma credo serva implementare degli observer da qualche parte sulla TUI
     */
    @Override
    public void dispatch(String message) {
       // System.out.println(message);
    }

    @Override
    public void dispatch(OnPlayerJoiningMessage onPlayerJoiningMessage, ServerInterface server) {
        System.out.println("--Received: checkForExistingGame: "+onPlayerJoiningMessage.getNoExistingGames());

    }

}
