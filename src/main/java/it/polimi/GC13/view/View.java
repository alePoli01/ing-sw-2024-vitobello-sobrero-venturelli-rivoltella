package it.polimi.GC13.view;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.InputException;
import it.polimi.GC13.model.Game;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface View {

    void tokenSetupPhase(int waitingPlayers, List<TokenColor> tokenColorList) throws IOException;

    void joiningPhase(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException;

    void startCardSetupPhase(TokenColor tokenColor) throws IOException;

    void exceptionHandler(InputException e);
}
