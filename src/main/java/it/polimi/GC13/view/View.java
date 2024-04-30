package it.polimi.GC13.view;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.inputException.InputException;
import it.polimi.GC13.model.Game;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface View {

    void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers);

    void handUpdate(int[] availableCard);

    void setPrivateObjectiveCard(int serialPrivateObjectiveCard);

    void setSerialPublicObjectiveCard(List<Integer> serialPublicObjectiveCard);

    void checkForExistingGame();

    void joiningPhase(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException;

    void joinExistingGame(Map<Game, Integer> waitingPlayersMap, Map<String, Game> joinableGameMap) throws IOException;

    void startCardSetupPhase(TokenColor tokenColor) throws IOException;

    void chosePrivateObjectiveCard(int readyPlayers, int neededPlayers, boolean isFlipped);

    void exceptionHandler(InputException e);

}
