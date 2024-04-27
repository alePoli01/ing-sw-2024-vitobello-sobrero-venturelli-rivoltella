package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;

import java.util.HashMap;
import java.util.Map;

public class OnCheckForExistingGameMessage implements MessagesFromServer {
    private final Map<String, Game> joinableGame = new HashMap<>();
    private final Map<Game, Integer> waitingPlayers = new HashMap<>();

    public OnCheckForExistingGameMessage(Map<String, Game> joinableGameMap, Map<Game, Integer> waitingPlayersMap) {
        this.joinableGame.putAll(joinableGameMap);
        this.waitingPlayers.putAll(waitingPlayersMap);
    }

    public Map<String, Game> getJoinableGameMap() {
        return joinableGame;
    }

    public Map<Game, Integer> getWaitingPlayersMap() {
        return waitingPlayers;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
