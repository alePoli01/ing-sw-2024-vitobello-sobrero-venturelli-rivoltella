package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;
import it.polimi.GC13.network.socket.messages.ServerMessage;

//implements ServerMessage because the interface has the dispatch method
public class PlayerJoiningMessage implements ServerMessage {
    /*
        all the message classes are used to both create the content of the message and to
        read the data for the controller
     */
    private final String playerName;

    public PlayerJoiningMessage(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerName() {
        return playerName;
    }

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) {
        //calls the server dispatcher using 'this' as parameter, the dispatcher called will know what to do
        //what the dispatch method will do changes based on the parameters ('this')
        serverDispatcher.dispatch(this,client);
    }
}
