package it.polimi.GC13.view;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface View {

    void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers);

    void handUpdate(String playerNickname, int[] availableCard);

    void setSerialCommonObjectiveCard(int[] serialCommonObjectiveCard);

    void updateGoldCardsAvailable(int ... goldCardSerial);

    void updateResourceCardsAvailable(int ... resourceCardSerial);

    void checkForExistingGame();

    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap);

    void startCardSetupPhase(String playerNickname, TokenColor tokenColor);

    void chosePrivateObjectiveCard(String playerNickname, int[] privateObjectiveCard);

    void onPositionedCard(String playerNickname, int startCardPlaced, boolean isFlipped);

    void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard);

    void drawCard();

    void showHomeMenu();

    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

    void displayTurns(Map<String, Position> playerPositions);

    void updateTurn(String playerNickname, boolean turn);

    void displayAvailableCells(Set<Coordinates> availableCells);

    void connectionLost();

    /*
            METHOD USED TO PLACE CARD ON THE BOARD
         */
    void placeCard();
}
