package it.polimi.GC13.view;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.util.List;
import java.util.Map;
import java.util.Set;

public interface View {

    String getGameName();

    String getNickname();

    void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) throws GenericException;

    void startView();

    void setVirtualServer(ServerInterface virtualServer);

    void handUpdate(String playerNickname, List<Integer> availableCard);

    void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard);

    void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial);

    void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceCardSerial);

    void checkForExistingGame(); //TODO: non lo uso nella GUI

    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) throws GenericException;

    void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) throws GenericException;

    void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) throws GenericException;

    void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn, List<Coordinates> availableCells);

    void setPrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers);

    void drawCard() throws GenericException;

    void showHomeMenu();

    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

    void setPlayersOrder(Map<String, Position> playerPositions);

    void reconnectToGame();

    void updateTurn(String playerNickname, boolean turn);

    void displayAvailableCells(List<Coordinates> availableCells);

    void onSetLastTurn(String playerNickname, Position position);

    void placeCard() throws GenericException;

    void updatePlayerScore(String playerNickname, int newPlayerScore);

    void onNewMessage(String sender, String receiver, String message);

    void gameOver(Set<String> winner);

    void onReconnectToGame();
}
