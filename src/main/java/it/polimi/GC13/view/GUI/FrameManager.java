package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.messages.fromclient.CheckForExistingGameMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.game.MainPage;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.TUI.BoardView;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.util.*;
import java.util.List;


public class FrameManager extends JFrame implements View {
    protected ServerInterface virtualServer;
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
    private final List<String> gamesLog = new ArrayList<>();
    private boolean cooking = false;
    private final Map<String, List<String>> chat = new HashMap<>();
    private boolean newMessage = false;
    private int choice = -1;

    private LoginFrame loginFrame;
    private MainPage gamePage;



    //NOTA BENE: property() per gestire il movimento dei token --> binding con i punteggi dei giocatori
    public FrameManager() {
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
            synchronized (this.hand){
                this.hand.clear();
                this.hand.addAll(availableCard);
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

    public ServerInterface getVirtualServer() {
        return this.virtualServer;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }


    @Override
    public void checkForExistingGame() {
        this.virtualServer.sendMessageFromClient(new CheckForExistingGameMessage());
    }


    /**
      JOINING PHASE
     * YES_OPTION --> create new game
     * NO_OPTION --> join an existing one
     */
    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) {
        if (gameNameWaitingPlayersMap.isEmpty()) {
            if(loginFrame == null) {
                SwingUtilities.invokeLater(() -> {
                    loginFrame = new LoginFrame(this);
                    loginFrame.setAlwaysOnTop(true);
                    loginFrame.repaint();
                    loginFrame.setAlwaysOnTop(false);
                });
            } else { // da testare
                refreshFrame(loginFrame);
            }
        } else {
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

            } else if (choice == JOptionPane.NO_OPTION){
                SwingUtilities.invokeLater(() -> loginFrame = new LoginFrame(this, gameNameWaitingPlayersMap));
            }
        }
        choice = -1;
    }

    @Override
    public String getGameName() {
        return this.gameName;
    }

    @Override
    public String getNickname() {
        return this.nickname;
    }

    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList, String gameName) {
        if (readyPlayers == neededPlayers) {
            this.gameName = gameName;
            this.loginFrame.dispose();
            if(gamePage == null){
                SwingUtilities.invokeLater(() -> {
                    gamePage = new MainPage(tokenColorList);
                    gamePage.setFrameManager(this);
                });
            } else{
                gamePage.getChoosePanel().removeAll();
                gamePage.showTokenChoose(tokenColorList);
                refreshFrame(gamePage);
                //gamePage.getChoosePanel().revalidate();
                //gamePage.getChoosePanel().repaint();
            }
        }
    }

    /**
     SETUP PHASE methods to the player
     startCardSetupPhase to chose which side to place your start card
     */


    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) {
        if(playerNickname.equals(this.nickname)) {
            setDataSetupPhase(playerNickname, tokenColor);
            gamePage.getPanelContainer().removeAll();
            gamePage.startCardSetup();

            //da rivedere refreshFrame
            gamePage.getContentPane().revalidate();
            gamePage.getContentPane().repaint();
        }
        this.gamesLog.add(playerNickname + " choose " + tokenColor + " token");
    }

    private void refreshFrame(JFrame frame){
        frame.getContentPane().revalidate();
        frame.getContentPane().repaint();
    }

    private void setDataSetupPhase(String playerNickname, TokenColor tokenColor){
        gamePage.setNickname(playerNickname);
        gamePage.setToken(tokenColor);
        gamePage.setHand(this.hand);
    }

    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            JOptionPane.showMessageDialog(this, onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
        }
        this.gamesLog.add(onInputExceptionMessage.getErrorMessage());
    }


    @Override
    public void setPlayersOrder(Map<String, Position> playerPositions) {
        this.playerPositions.putAll(playerPositions);
        gamePage.setPlayerPositions(playerPositions);
    }


    @Override
    public void updatePlayerScore(String playerNickname, int newPlayerScore) {
        this.playersScore.computeIfPresent(playerNickname, (key, oldValue) -> newPlayerScore);
        this.playersScore.putIfAbsent(playerNickname, newPlayerScore);
    }


    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn) {
        if (!this.playersBoard.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add( playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);

        //TODO: INSERIRE IL METODO PER PIAZZARE LA CARTA INIZIALE AL CENTRO DEL FRAME

        if (playerNickname.equals(this.nickname)) {
            if (serialCardPlaced <= 80) {
                //this.showHomeMenu();
            } /*else {
                System.out.println(message + ".\nWaiting for other players...");
            }*/
        }
    }

    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }


    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        if (playerNickname.equals(this.nickname)) {
            gamePage.getPanelContainer().removeAll();
            gamePage.setupObjectiveCard(serialCommonObjectiveCard, privateObjectiveCards);
            gamePage.getContentPane().revalidate();
            gamePage.getContentPane().repaint();
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
            //provare a impostare una label in waitingLobby in cui si indica il numero di giocatori pronti sul totale
        }
    }


    @Override
    public void updateTurn(String playerNickname, boolean turn) {
        if(playerNickname.equals(this.nickname)) {
            //System.out.println("figa");
            this.myTurn = turn;
            if(this.turnPlayed == 0){
                gamePage.getPanelContainer().removeAll();
                gamePage.createGamePanel();
                gamePage.getContentPane().revalidate();
                gamePage.getContentPane().repaint();
            }
            if(this.myTurn){
                gamePage.getTurnLable().setText("It's my turn!");
            }else {
                gamePage.getTurnLable().setText("waiting for my turn...");
            }
        }
        if (turn) {
            this.gamesLog.add("\nIt's " + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }
    }



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

    private void registerChatMessage(String key, String message) {
        if (this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(message);
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(key, new LinkedList<>(Collections.singletonList(message)));
            }
        }
        this.newMessage = true;
    }



    //METODO DA INVOCARE IN MAINPAGE
    public void receiveActionFromGame(int action){
        choice = action;
        showHomeMenu();
    }

    public void receiveActionFromGame(int action, String playerChosen){
        //viewPlayerBoard = playerChosen;
        choice = action;
        showHomeMenu();
        //viewPlayerBoard = "";
    }



    @Override
    public void showHomeMenu() {
        //System.out.println("\n--- HOME MENU ---");
        if (this.myTurn) {
            gamePage.getTurnLable().setText("It's your turn");
        } else {
            gamePage.getTurnLable().setText("It's not your turn");
        }

        //DA RIMUOVERE ALLA FINE
        /*
        System.out.println("\t[2] to place a card (only when in turn)");
        System.out.println("\t[6] to view other players' board");
        System.out.println("\t[7] to send a message in the chat");
        System.out.println("\t[8] to show game's history");
        System.out.println("\t[9] to show players' turns");
        System.out.println("\t[10] to draw card (only when in turn)");
        System.out.println("\t[12] to view chat [" + (this.newMessage ? "!" : "no new messages") + "]");*/


        switch (this.choice) {
            //place a card
            case 2: {
                this.placeCard();
                break;
            }
            //view other players' board
            case 6: {
                this.cooking = true;


                //System.out.println("Player chosen: " + playerChosen);
                //this.playersBoard.get(viewPlayerBoard); // modificare metodo per mostrare board su schermo


                this.cooking = false;
                break;
            }
            //send a message in the chat
            case 7: {
                this.cooking = true;
                this.sendMessage();
                this.cooking = false;
                break;
            }
            //show game's history
            case 8: {
                //this.printer.showHistory(this.gamesLog);
                break;
            }
            //show players' turns
            case 9: {

                System.out.println("Player's position are: ");
               // this.playerPositions.forEach((key, value) -> System.out.println(key + ": " + value));
                break;
            }
            //draw card
            case 10: {
                this.drawCard();
                break;
            }
            // view chat
            case 12: {
                //(this.newMessage ? "!" : "no new messages")
                this.cooking = true;
                synchronized (this.chat) {
                    //this.printer.seeChat(this.chat); //adattare il metodo alla gui
                    this.newMessage = false;
                }
                this.cooking = false;
                break;
            }
        }
        this.choice = -1;
    }



    private void sendMessage() {
   /*     try {
            this.cooking = true;
            String playerChosen;
            String message;
            do {
                System.out.println("Choose who to send the message to: [" + (String.join("], [", this.playersBoard.keySet()) + "]") + " or [global]");
                System.out.print("Player: ");
                playerChosen = reader.readLine();
                while (!(this.playersBoard.containsKey(playerChosen) || playerChosen.equals("global"))) {
                    System.out.print("Player " + playerChosen + " doesn't exist.\nEnter an existing player: ");
                    playerChosen = reader.readLine();
                }
                System.out.print("Write down your message: ");
                message = reader.readLine();
            } while (!(this.playersBoard.containsKey(playerChosen) || playerChosen.equals("global")));

            this.virtualServer.sendMessageFromClient(new NewMessage(this.nickname, playerChosen, message));
        } catch (IOException e) {
            System.err.println("Error parsing the name");
        }*/
    }



    //TODO: ANCORA DA FARE --------------------------------------------------------------------------------------------------


    public void gameHistory(){

    }







    @Override
    public void drawCard() {
     /*   this.printer.showDrawableCards(this.goldCardsAvailable, this.resourceCardsAvailable);
        if (this.myTurn && this.hand.size() == 2) {
            try {
                do {
                    System.out.print("Choose the card to withdraw: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                } while (!this.goldCardsAvailable.containsKey(this.choice) && !this.resourceCardsAvailable.containsKey(this.choice));
                this.virtualServer.drawCard(choice);
                System.out.println("+++ Sent draw card");
                this.choice = 0;
                this.turnPlayed++;
                System.out.println("You have passed the turn");
                this.printer.comeBack(this);
            } catch (IOException | NumberFormatException e) {
                System.out.println("Error: Please put a number");
            }
        } else {
            System.out.println("You cannot draw from the deck if it is not your turn or you didn't place one card on the board.");
            this.printer.comeBack(this);
        }*/
    }






    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {
      /*  System.out.println("Available cells are: ");
        System.out.println(availableCells);
        this.printer.comeBack(this);*/
    }



    @Override
    public void onSetLastTurn(String nickname, Position position) {
     /*   System.out.print(nickname + " has reached 20 points. The will be another turn for players in position: ");
        for (; playerPosition.ordinal() < this.playerPositions.size(); playerPosition.next(this.playerPositions.size())) {
            System.out.print(playerPosition + " ");
            System.out.println(" and another one bonus.");
        }*/
    }

    @Override
    public void placeCard() {
     /*   if (this.myTurn && this.hand.size() == 3) {
            int X = 0;
            int Y = 0;
            int serialCardToPlace = 0;
            boolean isFlipped = false;
            this.printer.showHand(this.hand);
            System.out.println("Enter serial card, X coordinate, Y coordinate, and [1] for FRONT [2] for BACK");
            System.out.println("example: " + this.hand.getFirst() + " (for serial card), 51 (for X), 51 (for Y), 1 (for side)");
            Scanner scanner = new Scanner(System.in);
            try {
                serialCardToPlace = scanner.nextInt();
                while (!this.hand.contains(serialCardToPlace)) {
                    System.out.println("You don't have the selected card. Available are: ");
                    this.hand.forEach(System.out::print);
                    serialCardToPlace = scanner.nextInt();
                }
                X = (scanner.nextInt());
                Y = (scanner.nextInt());
                isFlipped = scanner.nextInt() == 2;
            } catch (InputMismatchException e) {
                System.out.print("Error: Please input valid numbers.");
            }
            this.virtualServer.placeCard(serialCardToPlace, isFlipped, X, Y);
        } else if (!this.myTurn) {
            System.out.println("It's not your turn");
            this.printer.comeBack(this);
        } else {
            System.out.println("You have already placed a card. You need to draw a card to pass the turn.");
            this.printer.comeBack(this);
        }*/
    }

    @Override
    public void gameOver(Set<String> winner) {

    }
}
