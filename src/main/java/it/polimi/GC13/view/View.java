package it.polimi.GC13.view;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface View {

    void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers);

    void handUpdate(int[] availableCard);

    void setPrivateObjectiveCard(int serialPrivateObjectiveCard);

    void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard);

    void checkForExistingGame();

    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap);

    void startCardSetupPhase(String playerNickname, TokenColor tokenColor);

    void chosePrivateObjectiveCard(String player, int[] privateObjectiveCard);

    void displayPositionedCard(String player, int startCardPlaced, boolean isFlipped);

    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

}
