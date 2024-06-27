package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromclient.ReconnectPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromserver.ObjectiveAchieved;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.TUI.BoardView;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.URL;
import java.util.*;
import java.util.List;

import static it.polimi.GC13.view.GUI.game.CardManager.*;


//TODO: da rivedere JavaDoc

/**
 * The {@code FrameManager} class extends {@link JFrame} and implements {@link View}.
 * It manages the GUI components and user interactions for the game.
 * This class handles the player's actions, updates the game state, and communicates with the server.
 */
public class FrameManager extends JFrame implements View {
    private ServerInterface virtualServer;
    private String nickname;
    private String gameName;
    private final List<Integer> hand;
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard;
    private boolean myTurn;
    private int turnPlayed;
    private final Map<String, Integer> playersScore;
    private final Map<String, Position> playerPositions;
    private final Map<Integer, Boolean> goldCardsAvailable;
    private final Map<Integer, Boolean> resourceCardsAvailable;
    private final Map<String, BoardView> playersBoard;
    private final Map<String, EnumMap<Resource, Integer>> playersCollectedResources;
    private final List<String> gamesLog;
    private final Map<String, List<ChatMessage>> chat;
    private int newMessage;
    private boolean connectionOpen;
    private boolean firstTurn;
    private boolean showPopup;
    public List<Coordinates> availableCells;

    private LoginFrame loginFrame;
    private MainPage gamePage;
    private WinningFrame winningFrame;

    private final Map<String, TokenColor> tokenInGame;
    private int playerCounter;
    private int totalPlayers;

    private final ResourceGetter resourceGetter = new ResourceGetter();

    /**
     * Constructs a new {@code FrameManager} instance.
     * Initializes all the data structures and default values for the instance variables.
     */
    public FrameManager() {
        this.hand = new ArrayList<>();
        this.serialCommonObjectiveCard = new LinkedList<>();
        this.myTurn = false;
        this.turnPlayed = 0;
        this.playersScore = new HashMap<>();
        this.playerPositions = new HashMap<>();
        this.goldCardsAvailable = new HashMap<>();
        this.resourceCardsAvailable = new HashMap<>();
        this.playersBoard = new LinkedHashMap<>();
        this.playersCollectedResources = new LinkedHashMap<>();
        this.gamesLog = new ArrayList<>();
        this.chat = new LinkedHashMap<>();
        this.newMessage = 0;
        this.connectionOpen = true;
        this.firstTurn = true;
        this.showPopup = true;
        this.availableCells = new LinkedList<>();
        this.tokenInGame = new HashMap<>();
        this.playerCounter = 0;
    }

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Sets the nickname of the player.
     *
     * @param nickname the player's nickname
     */
    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public ServerInterface getVirtualServer() {
        return this.virtualServer;
    }

    @Override
    public void setVirtualServer(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
    }

    @Override
    public void startView() {
        this.checkForExistingGame();
    }

    /**
     * Used to update the player's hand in the GUI.
     *
     * @param playerNickname the player's nickname
     * @param availableCard the list of available cards
     */
    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if(playerNickname.equals(this.nickname)) {
            synchronized(this.hand) {
                this.hand.clear();
                this.hand.addAll(availableCard);

                if(!firstTurn){
                    gamePage.getFlipButton().setText("Show Back");
                    gamePage.refresh();
                    gamePage.setFlipToSend(false);
                    gamePage.getHandSerialNumberCheckBoxMap().clear();

                    if(!gamePage.isCheckingHandWhileDrawing()) {
                        Map<Integer, Boolean> printMap = new HashMap<>();
                        for (Integer integer : this.hand) printMap.put(integer, false);

                        gamePage.printHandOrDecksOnGUI(printMap, gamePage.getImagePanel(), gamePage.getCheckBoxPanel(),gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(),0,0);
                    }
                    refreshFrame(gamePage);
                }
            }
        }

        this.gamesLog.add(playerNickname + " has modified his hand;");
    }

    @Override
    public void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial) {
        this.goldCardsAvailable.clear();
        this.goldCardsAvailable.putAll(goldCardSerial);
    }

    @Override
    public void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceFacedUpSerial) {
        this.resourceCardsAvailable.clear();
        this.resourceCardsAvailable.putAll(resourceFacedUpSerial);
    }

    @Override
    public void checkForExistingGame() {
        this.virtualServer.sendMessageFromClient(new CheckForExistingGameMessage());
    }

    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) {
        int choice;
        if (gameNameWaitingPlayersMap.isEmpty()) {
            if (loginFrame != null) {
                loginFrame.dispose();
            }
            SwingUtilities.invokeLater(() -> {
                loginFrame = new LoginFrame(this);
                loginFrame.setAlwaysOnTop(true);
                loginFrame.repaint();
                loginFrame.setAlwaysOnTop(false);
            });
        } else {
            if (loginFrame!=null)
                loginFrame.dispose();

            Object[] options = {"Create Game", "Join Game"};

            JFrame jFrame = new JFrame();
            jFrame.setAlwaysOnTop(true);
            choice = JOptionPane.showOptionDialog(jFrame, "There are existing games, choose: ",
                    "Create Game / Join Game",
                    JOptionPane.YES_NO_OPTION,
                    JOptionPane.QUESTION_MESSAGE,
                    null, options,
                    options[0]);

            if (choice == JOptionPane.YES_OPTION) {
                SwingUtilities.invokeLater(() -> loginFrame = new LoginFrame(this));

            } else if (choice == JOptionPane.NO_OPTION) {
                SwingUtilities.invokeLater(() -> loginFrame = new LoginFrame(this, gameNameWaitingPlayersMap));
            }
        }
    }

    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) {
        if (readyPlayers == neededPlayers) {
            totalPlayers = neededPlayers;
            this.gameName = gameName;
            this.loginFrame.dispose();
            if (gamePage == null) {
                SwingUtilities.invokeLater(() -> {
                    gamePage = new MainPage(tokenColorList);
                    gamePage.setFrameManager(this);
                });
            } else {
                gamePage.getChoosePanel().removeAll();
                gamePage.showTokenChoose(tokenColorList);
                refreshFrame(gamePage);
            }
        } else {
            loginFrame.getWaitingLabel().setText("players in waiting room: " + readyPlayers + "/" + neededPlayers);
        }
    }

    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor){
        tokenInGame.put(playerNickname, tokenColor);
        if(playerNickname.equals(this.nickname)) {
            setDataSetupPhase(playerNickname, tokenColor);
            gamePage.getPanelContainer().removeAll();
            gamePage.startCardSetup();
            refreshFrame(gamePage);
        }
        this.gamesLog.add(playerNickname + " choose " + tokenColor + " token");
    }

    /**
     * Refreshes the given JFrame by revalidating and repainting its content pane.
     *
     * @param frame the JFrame to be refreshed
     */
    private void refreshFrame(JFrame frame) {
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    /**
     * Sets up the initial data for the player during the setup phase.
     *
     * @param playerNickname the nickname of the player
     * @param tokenColor     the token color assigned to the player
     */
    private void setDataSetupPhase(String playerNickname, TokenColor tokenColor) {
        gamePage.setNickname(playerNickname);
        gamePage.setToken(tokenColor);
    }

    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn, List<Coordinates> availableCells) {
        playerCounter++;
        if (!this.playersBoard.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add(playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);

        if (this.nickname.equals(playerNickname)) {
            this.availableCells.clear();
            this.availableCells.addAll(availableCells);

            if(serialCardPlaced <= 80) {
                if(gamePage.isUnableExceptionFlag()){
                    gamePage.afterCardPlaced();
                }
            } else {
                gamePage.getWaitingLabel().setText("Players ready: " + playerCounter + "/" + totalPlayers);
            }
        }
        this.gamePage.refreshBoard();
    }

    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }

    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            gamePage.getPanelContainer().removeAll();
            gamePage.getTimer().stop();
            gamePage.setupObjectiveCard(privateObjectiveCards);

            refreshFrame(gamePage);
        }
    }

    @Override
    public void setPrivateObjectiveCard(String playerNickname, int serialPrivateObjectiveCard, int readyPlayers, int neededPlayers) {
        if (playerNickname.equals(this.nickname)) {
            this.serialPrivateObjectiveCard = serialPrivateObjectiveCard;
            this.gamesLog.add("Your private objective card is " + serialPrivateObjectiveCard + ".");
        } else {
            this.gamesLog.add(playerNickname + " choose private objective card");
        }
        if (this.serialPrivateObjectiveCard != 0 && readyPlayers != neededPlayers) {
            gamePage.getWaitingLabel().setText("players that chose objective card: " + readyPlayers + "/" + neededPlayers);
        }
    }

    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            String titleString = "Launching an exception\n";
            JOptionPane.showMessageDialog(this, titleString + onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);

            if(gamePage.isUnableExceptionFlag()){
                gamePage.setCheckingHandWhileDrawing(false);
                gamePage.exceptionDetected();
            }
        }
        this.gamesLog.add(onInputExceptionMessage.getErrorMessage());
    }

    @Override
    public void setPlayersOrder(Map<String, Position> playerPositions) {
        this.playerPositions.putAll(playerPositions);
    }

    @Override
    public void onSetLastTurn(String nickname) {
        if(showPopup) {
            OnSetLastTurnDialog dialog = new OnSetLastTurnDialog(this, nickname);
            dialog.setVisible(true);
            showPopup = false;
        }
    }

    @Override
    public void updatePlayerScore(String playerNickname, int newPlayerScore) {
        this.playersScore.computeIfPresent(playerNickname, (key, oldValue) -> newPlayerScore);
        this.playersScore.putIfAbsent(playerNickname, newPlayerScore);

        for(String player : playersScore.keySet()){
            gamePage.getTokenManager().updatePlayerScore(player, playersScore.get(player));
        }

        gamePage.updateCrownImageInTable();
        gamePage.updateScoreColumn();

        if (this.nickname.equals(playerNickname)) {
            gamePage.getScoreLabel().setText("Score: " + playersScore.get(playerNickname));
            gamePage.getScoreLabel2().setText(gamePage.getScoreLabel().getText());
        }
    }

    @Override
    public void gameOver(Set<String> winners, Map<String, List<ObjectiveAchieved>> objectiveAchievedMap) {
        gamePage.dispose();
        SwingUtilities.invokeLater(()-> {
            winningFrame = new WinningFrame();
            if (winners.stream().anyMatch(winnerNickname -> winnerNickname.equals(this.nickname))) {
                winningFrame.getWinnerLabel().setText("you won!");
                winningFrame.getWinnerLabel().setForeground(new Color(61, 168, 52));
            } else {
                winningFrame.getWinnerLabel().setText("You lost!");
                winningFrame.getWinnerLabel().setForeground(new Color(205, 52, 17));
            }

            winningFrame.getScoreLabel().setText("Your score: " + this.getPlayersScore().get(this.nickname));
            winningFrame.showRankingImages(winners, this.getPlayersScore(), gamePage.getPlayersAvatarMap());
        });
    }

    @Override
    public void reconnectToGame() {
        this.virtualServer.sendMessageFromClient(new ReconnectPlayerToGameMessage(this.gameName, this.nickname));
    }

    @Override
    public void onReconnectToGame() {
        gamePage.getTimer().stop();
        gamePage.getContentPane().removeAll();
        gamePage.getContentPane().add(gamePage.getPanelContainer());
        refreshFrame(gamePage);
    }

    @Override
    public void updateCollectedResource(String playerNickname, EnumMap<Resource, Integer> collectedResources) {
        if (!this.playersCollectedResources.containsKey(playerNickname)) {
            this.playersCollectedResources.put(playerNickname, collectedResources);
            if (this.nickname.equals(playerNickname)) {
                gamePage.createTable(gamePage.getResourceTable(),new String[]{"Resources", "Amount"}, collectedResources, CardManager.logos, null, false, 24);
            }
        } else {
            this.playersCollectedResources.entrySet()
                    .stream()
                    .filter(playersMap -> playersMap.getKey().equals(playerNickname))
                    .forEach(playersMap -> playersMap.getValue().entrySet()
                            .stream()
                            .filter(entry -> !entry.getValue().equals(collectedResources.get(entry.getKey())))
                            .forEach(entry -> entry.setValue(collectedResources.get(entry.getKey()))));

            if (playerNickname.equals(this.nickname)){
                gamePage.updateResourceTable(gamePage.getResourceTable(), collectedResources, CardManager.logos);
            }
        }
    }

    @Override
    public void restartConnection(ServerInterface virtualServer, ConnectionBuilder connectionBuilder) {
        if (virtualServer == this.virtualServer) {
            int attemptCount = 0;       // after tot attempts ask to keep trying
            int sleepTime = 1000;       // initial delay
            int maxTime = 20000;        // caps the sleepTime
            int totalElapsedTime = 0;   // to be deleted
            double backOffBase = 1.05;  // changes the exponential growth of the time
            int t = 0;

            connectionOpen = false;
            while (!connectionOpen) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }

                try {
                    if (this.gameName == null || this.nickname == null) {
                        JOptionPane.showMessageDialog(this, "GameName or PlayerName is Null. \nThis program will be terminated", "ErrorMsg", JOptionPane.ERROR_MESSAGE, createResizedImageIcon(this.resourceGetter.getURL(ERROR_MONK), 140,180));
                        loginFrame.dispose();
                        System.exit(1);
                    }
                    if(t==0) {
                        gamePage.reconnectionWaitingPage();
                        t++;
                    }

                    // WHEN CONNECTION CLIENT <-> SERVER IS RESTORED, THE VIEW RECEIVES THE NEW VIRTUAL SERVER
                    this.virtualServer = connectionBuilder.createServerConnection(virtualServer.getClientDispatcher());
                    connectionOpen = true;
                    virtualServer.setConnectionOpen(true);
                    JOptionPane.showMessageDialog(this, "Connection restored, you can keep playing", "Return to Game", JOptionPane.INFORMATION_MESSAGE, createResizedImageIcon(this.resourceGetter.getURL(MONK4), 140,180));
                    this.reconnectToGame();
                } catch (IOException e) {
                    // exponential backoff algorithm
                    attemptCount++;
                    totalElapsedTime += sleepTime;
                    sleepTime = (int) Math.min(sleepTime * Math.pow(backOffBase, attemptCount), maxTime);

//                    gamePage.getNumAttemptLabel().setText(" ");
                    gamePage.getNumAttemptLabel().setText("Attempt #" + attemptCount + "   " + sleepTime + "ms    " + totalElapsedTime / 1000 + "s :");


                    if (attemptCount > 10) {
                        //after some attempts wait for user input
                        int answer = JOptionPane.showConfirmDialog(this, "Still waiting for Internet Connection \nDo you want to keep trying?", "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, createResizedImageIcon(this.resourceGetter.getURL(QUESTION_MONK), 140, 180));

                        if(answer == JOptionPane.YES_OPTION){
                            sleepTime = 2000;
                            attemptCount = 0;

                            revalidate();
                            repaint();
                        } else {
                            dispose();
                            System.exit(1);
                        }
                    }
                }
            }
        } else {
            System.out.println("Virtual server passed already updated");
        }
    }


    /**
     * Creates a resized ImageIcon from the given path and dimension.
     *
     * @param imagePath The path to the image
     * @param dimW The width dimension for resizing the image
     * @param dimH The height dimension for resizing the image
     * @return The resized ImageIcon
     */
    private ImageIcon createResizedImageIcon(URL imagePath, int dimW, int dimH) {
        return new ImageIcon(new ImageIcon(imagePath).getImage().getScaledInstance(dimW, dimH, Image.SCALE_SMOOTH));
    }


    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
            if (this.firstTurn) {
                gamePage.getPanelContainer().removeAll();
                gamePage.getNamePanel().removeAll();
                gamePage.refresh();
                gamePage.setUnableExceptionFlag(true);

                gamePage.createGamePanel();
                gamePage.getScoreLabel().setText("Score: " + 0);
                gamePage.getScoreLabel2().setText(gamePage.getScoreLabel().getText());

                Map<Integer, Boolean> printMap = new HashMap<>();
                for (Integer integer : this.hand) printMap.put(integer, false);

                gamePage.printHandOrDecksOnGUI(printMap, gamePage.getImagePanel(), gamePage.getCheckBoxPanel(), gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(), 0, 0);

                refreshFrame(gamePage);
                this.firstTurn = false;
            }
            if (this.myTurn) {
                gamePage.getTurnLabel().setText("It's my turn!");
                gamePage.getTurnLabel().setForeground(new Color(45, 114, 27));
                gamePage.getTurnLabel2().setText("It's my turn!");
                gamePage.getTurnLabel2().setForeground(new Color(45, 114, 27));

                gamePage.getHandLabelCheckBoxMap().values().forEach(checkBox -> checkBox.setEnabled(true));
            } else {
                gamePage.getTurnLabel().setText("waiting for my turn");
                gamePage.getTurnLabel().setForeground(new Color(175, 31, 31));
                gamePage.getTurnLabel2().setText("waiting for my turn");
                gamePage.getTurnLabel2().setForeground(new Color(175, 31, 31));
            }
        }
        if (turn) {
            this.gamesLog.add("\nIt's " + playerNickname + "'s turn");
        } else if (this.turnPlayed > 0) {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
    }


    /**
     * Saves a new message in the chat.
     *
     * @param sender message sender
     * @param recipient message recipient
     * @param content string that contains the message itself
     */
    @Override
    public void onNewMessage(String sender, String recipient, String content) {
        ChatMessage message = new ChatMessage(sender, content);

        // CASE A -> I AM THE RECIPIENT
        if (recipient.equals(this.nickname)) {
            this.registerChatMessage(sender, message);
        }
        // CASE B -> BROADCAST MESSAGE
        else if (recipient.equals("global")) {
            this.registerChatMessage("global", message);
        }
        // CASE C -> I AM THE SENDER
        else if (sender.equals(this.nickname)) {
            this.registerChatMessage(recipient, message);
        }
    }


    /**
     * * Registers a message in the chat map.
     *
     * @param key  the key to map the chat (global or player nickname)
     * @param message message sent in chat by the player
     */
    private void registerChatMessage(String key, ChatMessage message) {
        if(this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(message);
            }
        }else {
            synchronized (this.chat) {
                this.chat.put(key, new LinkedList<>(Collections.singletonList(message)));
            }
        }

        JLabel selectedLabel = (JLabel)gamePage.getCombobox().getSelectedItem();
        if (selectedLabel != null) {
            if(Objects.equals(selectedLabel.getText(), key)){
                gamePage.updateChatArea(key);
            } else {
                if(!gamePage.getNewMessageMap().get(key).getFirst()){
                    gamePage.getNewMessageMap().get(key).remove(gamePage.getNewMessageMap().get(key).getFirst());
                    gamePage.getNewMessageMap().get(key).add(true);
                } else {
                    gamePage.getNewMessageMap().get(key).add(true);
                }
                this.newMessage++;
                gamePage.changeComboBoxColor(key);
                gamePage.changeNotifyLabelImage();
            }
        }
    }

    @Override
    public void onClosingGame(String disconnectedPlayer) {
        String message;
        if(this.nickname.equals(disconnectedPlayer)) {
           message = "You seem to have lost connection, the game is being closed...";
        }else{
            message = "Player: " + disconnectedPlayer + " has disconnected, the game is being closed...";
        }
        JOptionPane.showMessageDialog(this, message, "Disconnection message", JOptionPane.ERROR_MESSAGE, createResizedImageIcon(this.resourceGetter.getURL(ERROR_MONK), 140, 180));
        System.exit(0);
    }


    //TODO: da fare
    public void gameHistory() {

    }

    /**
     * Retrieves the board views for all players.
     *
     * @return the map of players' board views, where the keys are player nicknames and values are BoardView objects
     */
    public Map<String, BoardView> getPlayersBoard() {
        return playersBoard;
    }

    /**
     * Retrieves the scores of all players.
     *
     * @return the map of players' scores, where keys are player nicknames and values are their scores
     */
    public Map<String, Integer> getPlayersScore() {
        return playersScore;
    }

    /**
     * Retrieves the positions of all players.
     *
     * @return the map of players' positions, where keys are player nicknames and values are their positions.
     */
    public Map<String, Position> getPlayerPositions() {
        return playerPositions;
    }

    /**
     * Retrieves the hand of the current player.
     *
     * @return the list of integers representing the player's hand
     */
    public List<Integer> getHand() {
        return hand;
    }

    /**
     * Retrieves the chat messages for all players.
     *
     * @return the map of chat messages, where keys can be player nicknames or "global", and values are lists of ChatMessage objects.
     */
    public Map<String, List<ChatMessage>> getChat() {
        return chat;
    }

    /**
     * Retrieves the list of serial numbers of common objective cards.
     *
     * @return the list of serial numbers of the common objective cards
     */
    public List<Integer> getSerialCommonObjectiveCard() {
        return serialCommonObjectiveCard;
    }

    /**
     * Retrieves the serial number of the private objective card for the current player.
     *
     * @return the serial number of the private objective card
     */
    public int getSerialPrivateObjectiveCard() {
        return serialPrivateObjectiveCard;
    }

    /**
     * Retrieves the gold cards available to draw.
     *
     * @return the map of available gold cards
     */
    public Map<Integer, Boolean> getGoldCardsAvailable() {
        return goldCardsAvailable;
    }

    /**
     * Retrieves the resource cards available to draw.
     *
     * @return the map of available resource cards, where keys are integers representing gold card's serial number and values indicate the side shown
     */
    public Map<Integer, Boolean> getResourceCardsAvailable() {
        return resourceCardsAvailable;
    }

    /**
     * Retrieves the number of turns played in the game.
     *
     * @return the number of turns played
     */
    public int getTurnPlayed() {
        return turnPlayed;
    }

    /**
     * Sets the number of turns played in the game.
     *
     * @param turnPlayed the number of turns played
     */
    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
    }

    /**
     * Checks if it is the current player's turn.
     *
     * @return true if it is the player's turn, false otherwise
     */
    public boolean isMyTurn() {
        return myTurn;
    }

    /**
     * Retrieves the collected resources for each player.
     *
     * @return the map of collected resources, where keys are player nicknames and values are
     * EnumMap objects containing resource types and amounts
     */
    public Map<String, EnumMap<Resource, Integer>> getPlayersCollectedResources() {
        return playersCollectedResources;
    }

    /**
     * Retrieves the token colors in the game.
     *
     * @return the map of tokens in the game, where keys are player nicknames and values
     * are TokenColor objects representing their token colors.
     */
    public Map<String, TokenColor> getTokenInGame() {
        return tokenInGame;
    }

    /**
     * Retrieves the count of new messages in the chat.
     *
     * @return the number of new messages in the queue, not visualized by the player
     */
    public int getNewMessage() {
        return newMessage;
    }

    /**
     * Sets the count of new messages in the chat.
     *
     * @param newMessage the number of new messages in the queue, not visualized by the player
     */
    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }

    //TODO: METODI NON USATI --------------------------------------------------------------------------------------------------

    @Override
    public void showHomeMenu() {}

    @Override
    public void placeCard() throws GenericException {}

    @Override
    public void drawCard() throws GenericException {}

    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {}

}