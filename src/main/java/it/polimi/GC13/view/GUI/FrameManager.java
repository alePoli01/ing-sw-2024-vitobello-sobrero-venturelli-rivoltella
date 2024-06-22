package it.polimi.GC13.view.GUI;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromclient.ReconnectPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.game.*;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.TUI.BoardView;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.util.*;
import java.util.List;


public class FrameManager extends JFrame implements View {
    private ServerInterface virtualServer;
    private String nickname;
    private String gameName;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard = new LinkedList<>();
    private boolean myTurn = false;
    private int turnPlayed = 0;
    private final Map<String, Integer> playersScore = new HashMap<>();
    private final Map<String, Position> playerPositions = new HashMap<>();
    private final Map<Integer, Boolean> goldCardsAvailable = new HashMap<>();
    private final Map<Integer, Boolean> resourceCardsAvailable = new HashMap<>();
    private final Map<String, BoardView> playersBoard = new LinkedHashMap<>();
    private final Map<String, EnumMap<Resource, Integer>> playersCollectedResources = new LinkedHashMap<>();
    private final List<String> gamesLog = new ArrayList<>();
    private final Map<String, List<ChatMessage>> chat = new LinkedHashMap<>();
    private int newMessage = 0;
    private boolean firstTurn = true;
    private boolean showPopup = true;
    public List<Coordinates> availableCells = new LinkedList<>();

    private LoginFrame loginFrame;
    private MainPage gamePage;
    private WinningFrame winningFrame;

    private final Map<String, TokenColor> tokenInGame = new HashMap<>();
    private int playerCounter = 0;
    private int totalPlayers;

    //TODO:
    // + DA TESTARE NELLE VARE COMBINAZIONI TUI-GUI E RMI-SOCKET (RICERCA ERRORI)
    // + DA IMPOSTARE SFONDI


    public FrameManager() {}

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

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
     used to update players hand in GUI
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

                        gamePage.printHandOrDecksOnGUI(printMap, gamePage.getTokenPanel(), gamePage.getCheckBoxPanel(),gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(),0,0);
                    }
                    refreshFrame(gamePage);
                }
            }
        }
        //mostro anche le mie azioni (da testare)
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


    /**
     * JOINING PHASE
     * YES_OPTION --> create new game
     * NO_OPTION --> join an existing one
     */
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

    /**
     SETUP PHASE
     token choice when all players joined the game
     waiting when readPlayers < neededPlayers
     */
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

    /**
     * SETUP PHASE methods to the player
     * startCardSetupPhase to chose which side to place your start card
     */
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

    private void refreshFrame(JFrame frame) {
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private void setDataSetupPhase(String playerNickname, TokenColor tokenColor) {
        gamePage.setNickname(playerNickname);
        gamePage.setToken(tokenColor);
    }


    /**
     NOTIFY RESPECTIVE CLIENT WHEN A CARD IS PLACED ON ANY BOARD
     OTHERS -> ADDS TO LOG OPERATION
     */
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

    /**
     NOTIFY THE CLIENTS ABOUT THE COMMON OBJECTIVE CARD
     */
    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }

    /**
     METHOD THAT ALLOW THE CLIENT TO CHOOSE HIS OBJECTIVE CARD
     */
    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            gamePage.getPanelContainer().removeAll();
            gamePage.getTimer().stop();
            gamePage.setupObjectiveCard(privateObjectiveCards);

            refreshFrame(gamePage);
        }
    }

    /**
     NOTIFY THE CORRECT CLIENT AFTER THE MODEL UPDATED THE PLAYER'S PRIVATE OBJECTIVE CARD
     */
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

    /**
     METHOD USED TO GIVE EACH USER VISIBILITY OF PLAYERS ORDER
     */
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
    public void gameOver(Set<String> winners) {
        gamePage.dispose();
        SwingUtilities.invokeLater(()-> {
            winningFrame = new WinningFrame();
            if (winners.stream().anyMatch(winnerNickname -> winnerNickname.equals(this.nickname))) {
                winningFrame.getWinnerLabel().setText("You win!");
                winningFrame.getWinnerLabel().setForeground(new Color(61, 168, 52));
            } else {
                winningFrame.getWinnerLabel().setText("You lose!");
                winningFrame.getWinnerLabel().setForeground(new Color(205, 52, 17));
            }

            winningFrame.getScoreLabel().setText("Your score: " + this.getPlayersScore().get(this.nickname));
            winningFrame.showRankingImages(winners, this.getPlayersScore(), gamePage.getPlayersAvatarMap());
        });
    }


    //testing
    public void gameOver2(MainFrameProva parent, Set<String> winners) {
        parent.dispose();
        SwingUtilities.invokeLater(()-> {
            winningFrame = new WinningFrame();

            if (winners.stream().anyMatch(winnerNickname -> winnerNickname.equals(parent.getNickname()))) {
                winningFrame.getWinnerLabel().setText("you won!");
                winningFrame.getWinnerLabel().setForeground(new Color(61, 168, 52));
            } else {
                winningFrame.getWinnerLabel().setText("you lose!");
                winningFrame.getWinnerLabel().setForeground(new Color(205, 52, 17));
            }

            winningFrame.getScoreLabel().setText("your score: " + parent.getPlayersScore().get(parent.getNickname()));
            winningFrame.showRankingImages(winners, parent.getPlayersScore(), parent.getPlayersAvatarMap());
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
                gamePage.createTable(gamePage.getResourceTable(),new String[]{"Resources", "Amount"}, collectedResources, CardManager.logos, null, false, 25);
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
            boolean connectionOpen = false;

            gamePage.reconnectionWaitingPage();

            while (!connectionOpen) {
                try {
                    Thread.sleep(sleepTime);
                } catch (InterruptedException ex) {
                    throw new RuntimeException(ex);
                }
                try {
                    if (this.gameName == null || this.nickname == null) {
                        JOptionPane.showMessageDialog(this, "GameName or PlayerName is Null", "ErrorMsg", JOptionPane.ERROR_MESSAGE, createResizedTokenImageIcon(CardManager.ERROR_MONK, 140));
                        gamePage.dispose();
                        System.exit(1);
                    }
                    // WHEN CONNECTION CLIENT <-> SERVER IS RESTORED, THE VIEW RECEIVES THE NEW VIRTUAL SERVER
                    this.virtualServer = connectionBuilder.createServerConnection(virtualServer.getClientDispatcher());
                    //this.setVirtualServer(this.virtualServer);
                    virtualServer.setConnectionOpen(true);
                    connectionOpen = true;
                    JOptionPane.showMessageDialog(this, "Connection restored, you can keep playing", "Return to Game", JOptionPane.INFORMATION_MESSAGE, createResizedTokenImageIcon(CardManager.MONK4, 140));
                    this.reconnectToGame(); //se tutto va bene da qui si va a onReconnectToGame
                } catch (IOException e) {
                    // exponential backoff algorithm
                    attemptCount++;
                    totalElapsedTime += sleepTime;
                    sleepTime = (int) Math.min(sleepTime * Math.pow(backOffBase, attemptCount), maxTime);

                    gamePage.getNumAttemptLabel().setText("Attempt #" + attemptCount + "   " + sleepTime + "ms    " + totalElapsedTime / 1000 + "s :");

                    if (attemptCount > 10) {
                        //after some attempts wait for user input
                        int answer = JOptionPane.showConfirmDialog(this, "Still waiting for Internet Connection \nDo you want to keep trying?", "Continue?", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, createResizedTokenImageIcon(CardManager.QUESTION_MONK, 140));

                        if(answer == JOptionPane.YES_OPTION){
                            sleepTime = 2000;
                            attemptCount = 0;

                            //SERVE?
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

    @Override
    public void onClosingGame(String disconnectedPlayer) {

    }

    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
    }


    /**
     NOTIFY RESPECTIVE CLIENT WHEN IT'S THEIR TURN
     OTHERS -> ADDS TO LOG OPERATION
     */
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

                gamePage.printHandOrDecksOnGUI(printMap, gamePage.getTokenPanel(), gamePage.getCheckBoxPanel(), gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(), 0, 0);

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
     * method to save new a message in the chat
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
     *
     * @param key can be [global] or a player [nickname], it is used to map the chat
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
    public void showHomeMenu() {}

    @Override
    public void placeCard() throws GenericException {}

    @Override
    public void drawCard() throws GenericException {}

    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {}



    //TODO: ANCORA DA FARE --------------------------------------------------------------------------------------------------


    public void gameHistory() {

    }

    public Map<String, BoardView> getPlayersBoard() {
        return playersBoard;
    }

    public Map<String, Integer> getPlayersScore() {
        return playersScore;
    }

    public Map<String, Position> getPlayerPositions() {
        return playerPositions;
    }

    public List<Integer> getHand() {
        return hand;
    }

    public Map<String, List<ChatMessage>> getChat() {
        return chat;
    }

    public List<Integer> getSerialCommonObjectiveCard() {
        return serialCommonObjectiveCard;
    }

    public int getSerialPrivateObjectiveCard() {
        return serialPrivateObjectiveCard;
    }

    public Map<Integer, Boolean> getGoldCardsAvailable() {
        return goldCardsAvailable;
    }

    public Map<Integer, Boolean> getResourceCardsAvailable() {
        return resourceCardsAvailable;
    }

    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
    }

    public boolean isMyTurn() {
        return myTurn;
    }

    public Map<String, EnumMap<Resource, Integer>> getPlayersCollectedResources() {
        return playersCollectedResources;
    }

    public Map<String, TokenColor> getTokenInGame() {
        return tokenInGame;
    }

    public int getNewMessage() {
        return newMessage;
    }

    public void setNewMessage(int newMessage) {
        this.newMessage = newMessage;
    }
}