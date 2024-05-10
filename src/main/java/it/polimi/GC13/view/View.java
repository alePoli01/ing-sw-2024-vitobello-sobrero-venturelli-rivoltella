package it.polimi.GC13.view;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.util.List;
import java.util.Map;

public interface View {

    void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList);

    void handUpdate(String playerNickname, List<Integer> availableCard);

    void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard);

    void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial);

    void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceCardSerial);

    void checkForExistingGame();

    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap);

    void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor);

    void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards);

    void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn);

    void setPrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers);

    void drawCard();

    void showHomeMenu();

    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

    void setPlayersOrder(Map<String, Position> playerPositions);

    void updateTurn(String playerNickname, boolean turn);

    void displayAvailableCells(List<Coordinates> availableCells);

    void connectionLost();

    void onSetLastTurn(String playerNickname, Position position);

    /*
        METHOD USED TO PLACE CARD ON THE BOARD
    */
    void placeCard();

    void updatePlayerScore(String playerNickname, int newPlayerScore);

    void onNewMessage(String sender, String receiver, String message);
}
