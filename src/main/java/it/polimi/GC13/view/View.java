package it.polimi.GC13.view;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.io.IOException;
import java.util.Map;

public interface View {

    void setupPhase(int waitingPlayers) throws IOException;

    void display(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException;

    void printExceptionError(Exception e);
}
