package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

import java.util.List;

public class OnPlayerJoiningMessage implements MessagesFromServer{
    private final List<Player> playerList;
    private final String message;
    private final Boolean noExistingGames;

    public OnPlayerJoiningMessage(List<Player> playerList, String message,Boolean noExistingGames) {
        this.message=message;
        this.playerList=playerList;
        this.noExistingGames=noExistingGames;
    }

    public Boolean getNoExistingGames() {
        return noExistingGames;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher, ServerInterface server) {
        clientDispatcher.dispatch(this,server);
    }
}
