package it.polimi.GC13.view;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromserver.ObjectiveAchieved;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public interface View {

    /**
     * Gets the game name.
     *
     * @return the name of the game
     */
    String getGameName();

    /**
     * Gets the player's nickname.
     *
     * @return the player's nickname
     */
    String getNickname();

    /**
     * Handles the token choice setup phase when all players have joined the game.
     *
     * @param readyPlayers the number of players ready
     * @param neededPlayers the number of players needed to start the game
     * @param tokenColorList the list of available token colors
     * @param gameName the name of the game
     * @throws GenericException if an error occurs during token selection
     */
    void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) throws GenericException;

    /**
     * Starts the view of the game.
     */
    void startView();

    /**
     * Sets the virtual server.
     *
     * @param virtualServer the virtual server to be set
     */
    void setVirtualServer(ServerInterface virtualServer);

    /**
     * Updates a player's hand.
     *
     * @param playerNickname the nickname of the player
     * @param availableCard the list of available cards to set in the player's hand
     */
    void handUpdate(String playerNickname, List<Integer> availableCard);

    /**
     * Sets the serial numbers of common objective cards.
     *
     * @param serialCommonObjectiveCard the list of common objective cards' serial numbers of the game
     */
    void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard);

    /**
     * Updates the list of gold cards available to draw.
     *
     * @param goldCardSerial a map of gold card serial numbers and the shown side
     */
    void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial);

    /**
     * Updates the resource cards available to draw.
     *
     * @param resourceCardSerial a map of resource card serial numbers and the shown side
     */
    void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceCardSerial);

    /**
     * Checks for existing games previously created and still in the joining phase.
     */
    void checkForExistingGame();

    /**
     * Manages the joining phase of a game.
     *
     * @param gameNameWaitingPlayersMap a map of game names and the number of waiting players.
     * @throws GenericException if an error occurs during the joining phase.
     */
    void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) throws GenericException;

    /**
     * Handles the start card setup phase.
     *
     * @param playerNickname the nickname of the player
     * @param tokenColor the color of the player's token
     * @throws GenericException if an error occurs during card placement
     */
    void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) throws GenericException;

    /**
     * Manages the choice of the private objective card.
     *
     * @param playerNickname the nickname of the player
     * @param privateObjectiveCards the list of available private objective cards
     * @throws GenericException if an error occurs during card selection
     */
    void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) throws GenericException;

    /**
     * Handles the event when a card is placed by a player.
     *
     * @param playerNickname the nickname of the player
     * @param serialCardPlaced the serial number of the placed card
     * @param isFlipped whether the card is flipped or not
     * @param x the x-coordinate of the card placement
     * @param y the y-coordinate of the card placement
     * @param turn the current turn number
     * @param availableCells the list of available cells for the next placement
     */
    void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn, List<Coordinates> availableCells);

    /**
     * Sets the selected private objective card for a player.
     *
     * @param playerNickname the nickname of the player
     * @param indexPrivateObjectiveCard the index of the selected private objective card
     * @param readyPlayers the number of players ready
     * @param neededPlayers the number of players needed to start the game
     */
    void setPrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers);


    void drawCard() throws GenericException; //TODO: non lo uso in GUI


    void showHomeMenu(); //TODO: non lo uso in GUI

    /**
     * Handles exceptions that occur on input.
     *
     * @param playerNickname the nickname of the player
     * @param onInputExceptionMessage the exception message
     */
    void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage);

    /**
     * Sets the players' order in the game.
     *
     * @param playerPositions a map of player nicknames and their positions
     */
    void setPlayersOrder(Map<String, Position> playerPositions);

    /**
     * Reconnects the player to the game.
     */
    void reconnectToGame();

    /**
     * Updates the turn for a player.
     *
     * @param playerNickname the nickname of the player
     * @param turn whether it is the player's turn or not
     */
    void updateTurn(String playerNickname, boolean turn);


    void displayAvailableCells(List<Coordinates> availableCells); //TODO: non lo uso in GUI

    /**
     * Notify the players when someone reaches 20 points and start the last round.
     *
     * @param playerNickname the nickname of the player that reaches 20 points
     */
    void onSetLastTurn(String playerNickname);


    void placeCard() throws GenericException; //TODO: non lo uso in GUI

    /**
     * Updates the player's score.
     *
     * @param playerNickname the nickname of the player
     * @param newPlayerScore the new score of the player
     */
    void updatePlayerScore(String playerNickname, int newPlayerScore);

    /**
     * method to save new a message in the chat.
     *
     * @param sender the sender of the message
     * @param receiver the receiver of the message
     * @param message the content of the message
     */
    void onNewMessage(String sender, String receiver, String message);

    /**
     * Handles the game over event.
     *
     * @param winner the set of winning players' nicknames
     */
    void gameOver(Set<String> winner, Map<String, List<ObjectiveAchieved>> objectiveAchievedMap);

    /**
     * Handles the client's reconnection to the game.
     */
    void onReconnectToGame();

    /**
     * Updates the collected resources for a player.
     *
     * @param playerNickname the nickname of the player
     * @param collectedResources the map of collected resources
     */
    void updateCollectedResource(String playerNickname, EnumMap<Resource, Integer> collectedResources);

    /**
     * Restart the connection to the server.
     *
     * @param virtualServer the virtual server
     * @param connectionBuilder the connection builder
     */
    void restartConnection(ServerInterface virtualServer, ConnectionBuilder connectionBuilder);

    /**
     * Handles the event of closing a game due to a player disconnection.
     *
     * @param disconnectedPlayer the nickname of the disconnected player
     */
    void onClosingGame(String disconnectedPlayer);
}
