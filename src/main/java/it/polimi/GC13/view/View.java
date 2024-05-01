package it.polimi.GC13.view;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.util.List;
import java.util.Map;

public interface View {

    void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers);

    void handUpdate(String playerNickname, int[] availableCard);

    void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard);

    void checkForExistingGame();

    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap);

    void startCardSetupPhase(String playerNickname, TokenColor tokenColor);

    void chosePrivateObjectiveCard(String playerNickname, int[] privateObjectiveCard);

    void onPositionedCard(String playerNickname, int startCardPlaced, boolean isFlipped);

    void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard);

    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

}
