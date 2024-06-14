package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromclient.NewMessage;
import it.polimi.GC13.network.messages.fromclient.ReconnectPlayerToGameMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.game.*;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.TUI.BoardView;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.awt.*;
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
    private boolean cooking = false;
    private final Map<String, List<String>> chat = new HashMap<>();
    private boolean newMessage = false;
    public List<Coordinates> availablesCells = new LinkedList<>();
    private boolean newStatus = false;
    private boolean firstTurn = true;


    private LoginFrame loginFrame;
    private MainPage gamePage;
    private WinningFrame winningFrame;

    private final Map<String, TokenColor> tokenInGame = new HashMap<>();


    //TODO:
    // + DA RIDIMENSIONARE TUTTO IN BASE ALLA RISOLUZIONE DELLO SCHERMO (1920X1080)
    // + DA FARE LA CHAT (TEXTAREA + JCOMBOBOX LA SELEZIONE DEL GIOCATORE CON CUI CHATTARE)
    // + DA TESTARE NELLE VARE COMBINAZIONI TUI-GUI E RMI-SOCKET (RICERCA ERRORI)
    // + DA GESTIRE LA DISCONNESSIONE DEL SERVER (IN FRAMEMANAGER)
    // + DA MIGLIORARE VISIVAMENTE IL POPUP DI onSetLastTurn (PROVA VISIBILE IN MAINFRAMEPROVA AL CLICK DEL TASTO POPUP)
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

    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if (playerNickname.equals(this.nickname)) {
            synchronized (this.hand) {
                this.hand.clear();
                this.hand.addAll(availableCard);

                if(gamePage.getFlag()>0){
                    gamePage.getFlipButton().setText("Show Back");
                    gamePage.refresh();
                    gamePage.setFlipToSend(false);
                    gamePage.getHandSerialNumberCheckBoxMap().clear();

                    if (!gamePage.isCheckingHandWhileDrawing()) {
                        ArrayList<Boolean> b = new ArrayList<>();
                        for(int i=0; i< this.hand.size(); i++)
                            b.add(false);

                        gamePage.printHandOrDecksOnGUI(this.hand, b, gamePage.getTokenPanel(), gamePage.getCheckBoxPanel(),gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(),0,0);
                    }

                    gamePage.setFlag(gamePage.getFlag()+1);
                    refreshFrame(gamePage);
                }
            }
        } else {
            this.gamesLog.add(playerNickname + " has drawn a card");
        }
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

    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) {
        if (readyPlayers == neededPlayers) {
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
        }
    }

    /**
     * SETUP PHASE methods to the player
     * startCardSetupPhase to chose which side to place your start card
     */
    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) throws GenericException {
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
        if (!this.playersBoard.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add(playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);

        if (this.nickname.equals(playerNickname)) {
            this.availablesCells.clear();
            this.availablesCells.addAll(availableCells);

            if(gamePage.isUnableExceptionFlag()){
                gamePage.afterCardPlaced();
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
            //provare a impostare una label in waitingLobby in cui si indica il numero di giocatori pronti sul totale
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
        OnSetLastTurnDialog dialog = new OnSetLastTurnDialog(this, nickname);
        dialog.setVisible(true);
        this.newStatus = true;
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
            System.out.println(playersScore.get(playerNickname));
        }
    }


    /**
     * method to save new a message in the chat
     * @param sender message sender
     * @param recipient message recipient
     * @param message string that contains the message itself
     */
    @Override
    public void onNewMessage(String sender, String recipient, String message) {
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


    @Override
    public void gameOver(Set<String> winner) {
        gamePage.dispose();
        SwingUtilities.invokeLater(()-> {
            winningFrame = new WinningFrame();
            if (winner.stream().anyMatch(winnerNickname -> winnerNickname.equals(this.nickname))) {
                winningFrame.getWinnerLabel().setText("You win!");
                winningFrame.getWinnerLabel().setForeground(new Color(61, 168, 52));
            } else {
                winningFrame.getWinnerLabel().setText("You lose!");
                winningFrame.getWinnerLabel().setForeground(new Color(205, 52, 17));
            }
        });
    }


    //testing
    public void gameOver2(MainFrameProva parent, Set<String> winner) {
        parent.dispose();
        SwingUtilities.invokeLater(()-> {
            winningFrame = new WinningFrame();

            if (winner.stream().anyMatch(winnerNickname -> winnerNickname.equals(parent.getNickname()))) {
                winningFrame.getWinnerLabel().setText("You win!");
                winningFrame.getWinnerLabel().setForeground(new Color(61, 168, 52));
            } else {
                winningFrame.getWinnerLabel().setText("You lose!");
                winningFrame.getWinnerLabel().setForeground(new Color(205, 52, 17));
            }

            winningFrame.getScoreLabel().setText("Your score: " + parent.getPlayersScore().get(parent.getNickname()));


        });
    }


    @Override
    public void reconnectToGame() {
        this.virtualServer.sendMessageFromClient(new ReconnectPlayerToGameMessage(this.gameName, this.nickname));
    }

    @Override
    public void onReconnectToGame() {
        this.newStatus = true;
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

    private void sendMessage() {

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
                gamePage.setFlag(1);
                gamePage.setUnableExceptionFlag(true);

                gamePage.createGamePanel();
                gamePage.getScoreLabel().setText("Score: " + 0);
                gamePage.getScoreLabel2().setText(gamePage.getScoreLabel().getText());

                ArrayList<Boolean> b = new ArrayList<>();
                for(int i=0; i<hand.size(); i++)
                    b.add(false);

                gamePage.printHandOrDecksOnGUI(hand, b, gamePage.getTokenPanel(), gamePage.getCheckBoxPanel(),gamePage.getChoosePanel(), gamePage.getButtonGroup(), gamePage.getHandLabelCheckBoxMap(), gamePage.getHandSerialNumberCheckBoxMap(),0,0);

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
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
    }



    /**
     *
     * @param key can be [global] or a player [nickname], it is used to map the chat
     * @param message message sent in chat by the player
     */

    //uguale/molto simile a tui (mettere in View?)
    private void registerChatMessage(String key, String message) { // uguale + update gui
        if (this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(message);
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(key, new LinkedList<>(Collections.singletonList(message)));
            }
        }
        this.newMessage = true; //update gui trigger

        //if --> controllo se GUi mostra la chat del mittente del messaggio arrivato --> se si: updateTextArea

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

    public Map<String, List<String>> getChat() {
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
}