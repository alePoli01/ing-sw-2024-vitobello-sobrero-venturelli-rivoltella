package it.polimi.GC13.network.socket.messages.fromclient;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ServerDispatcherInterface;

import java.io.IOException;
import java.io.Serializable;

//implements ServerMessage because the interface has the dispatch method
public class PlayerJoiningMessage implements MessagesFromClient{
    /*
        all the message classes are used to both create the content of the message and to
        read the data for the controller
     */
    private final Player player;
    private final int numOfPlayers;
    public PlayerJoiningMessage(String nickname, int numOfPlayers) {
        this.player = new Player(nickname);
        this.numOfPlayers = numOfPlayers;
    }

    public Player getPlayer() {
        return player;
    }

    public int getNumOfPlayers() {
        return numOfPlayers;
    }

    @Override
    public void dispatch(ServerDispatcherInterface serverDispatcher, ClientInterface client) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {
        //calls the server dispatcher using 'this' as parameter, the dispatcher called will know what to do
        //what the dispatch method will do changes based on the parameters ('this')
        System.out.println("PlayerJoiningMessage received: " + player.getNickname());
        serverDispatcher.dispatch(this, client);
    }
}
