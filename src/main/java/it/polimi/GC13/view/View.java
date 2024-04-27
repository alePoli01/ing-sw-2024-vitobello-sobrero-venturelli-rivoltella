package it.polimi.GC13.view;

import it.polimi.GC13.model.Game;
import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.io.IOException;
import java.util.Map;

public interface View {

    void setupPhase(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage, int waitingPlayers) throws IOException;

    void display(OnCheckForExistingGameMessage onCheckForExistingGameMessage, Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException;

    void printExceptionError(Exception e);
}
