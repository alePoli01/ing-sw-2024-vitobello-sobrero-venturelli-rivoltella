package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

//  implements ServerMessage because the interface has the dispatch method

public record AddPlayerToGameMessage(String playerNickname, int numOfPlayers, String gameName) implements MessagesFromClient {

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException {
        //calls the server dispatcher using 'this' as parameter, the dispatcher called will know what to do
        //what the dispatch method will do changes based on the parameters ('this')
        serverDispatcher.dispatch(this, client);
    }
}
