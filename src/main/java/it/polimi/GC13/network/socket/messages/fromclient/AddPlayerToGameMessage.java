package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;

/**
 * @param player all the message classes are used to both create the content of the message and to
 *               read the data for the controller
 */
//  implements ServerMessage because the interface has the dispatch method

public record AddPlayerToGameMessage(Player player, int numOfPlayers, String gameName) implements MessagesFromClient {

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException {
        //calls the server dispatcher using 'this' as parameter, the dispatcher called will know what to do
        //what the dispatch method will do changes based on the parameters ('this')
        serverDispatcher.dispatch(this, client);
    }
}
