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
import java.awt.*;
import java.util.*;
import java.util.List;


public class FrameManager extends JFrame implements View {
    protected ServerInterface virtualServer;
    private String nickname;
    private final List<Integer> hand = new ArrayList<>();
    private int serialPrivateObjectiveCard;
    private List<Integer> serialCommonObjectiveCard = new LinkedList<>();
    private boolean myTurn = false;
    private int turnPlayed = - 1;
    private final Map<String, Integer> playersScore = new HashMap<>();
    private final Map<String, Position> playerPositions = new HashMap<>();
    private final Map<Integer, Boolean> goldCardsAvailable = new HashMap<>();
    private final Map<Integer, Boolean> resourceCardsAvailable = new HashMap<>();
    private final Map<String, BoardView> playersBoard = new LinkedHashMap<>();
    private final List<String> gamesLog = new ArrayList<>();
    private boolean cooking = false;
    private final Map<String, List<String>> chat = new HashMap<>();
    private boolean newMessage = false;

    private LoginFrame loginFrame;
    private MainPage gamePage;



    //TODO: capire come piazzare il frame in primo piano
    // NOTA BENE: property() per gestire il movimento dei token --> binding con i punteggi dei giocatori

    public FrameManager() {
        this.virtualServer = null;
    }

    @Override
    public void startView() {
        this.checkForExistingGame();
    }

    @Override
    public void setVirtualServer(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
    }

    public ServerInterface getVirtualServer() {
        return this.virtualServer;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {
        if (playerNickname.equals(this.nickname)) {
            synchronized (this.hand){
                this.hand.clear();
                this.hand.addAll(availableCard);
                gamePage.setHand(this.hand);
            }
        } else {
            //this.gamesLog.add(playerNickname + " has drawn a card");
        }
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
        int choice = -1; //da capire se va bene renderlo parametro locale
        if (gameNameWaitingPlayersMap.isEmpty()) {
            SwingUtilities.invokeLater(() -> {
                loginFrame = new LoginFrame(this);

                loginFrame.setAlwaysOnTop(true);
                loginFrame.repaint();
                loginFrame.setAlwaysOnTop(false);

            });

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
                //loginFrame = new LoginFrame(this);

            } else if (choice == JOptionPane.NO_OPTION){
                SwingUtilities.invokeLater(() -> loginFrame = new LoginFrame(this, gameNameWaitingPlayersMap));
            }
        }
        choice = -1;
    }



    //TODO: aggiungere il numero di giocatori in attesa nella waiting room
    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList) {
        if (readyPlayers == neededPlayers) {
            this.loginFrame.dispose();
            if(gamePage == null){
                SwingUtilities.invokeLater(() -> gamePage = new MainPage(this, tokenColorList));
            } else{
                gamePage.getChoosePanel().removeAll();
                gamePage.showTokenChoose(tokenColorList);
                gamePage.getChoosePanel().revalidate();
                gamePage.getChoosePanel().repaint();
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
            System.out.println("Sega");
            gamePage.setNickname(playerNickname);
            gamePage.setToken(tokenColor);
            gamePage.getPanelContainer().removeAll();
            gamePage.createGamePanel();
            refreshGamePage();
        }
    }

    private void refreshGamePage(){
        gamePage.getContentPane().revalidate();
        gamePage.getContentPane().repaint();
    }

    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {
        if (playerNickname.equals(this.nickname)) {
            JOptionPane.showMessageDialog(this, onInputExceptionMessage.getErrorMessage());
            onInputExceptionMessage.methodToRecall(this);
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
    public void setPlayersOrder(Map<String, Position> playerPositions) {
        this.playerPositions.putAll(playerPositions);

    }

    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {
        this.serialCommonObjectiveCard = serialCommonObjectiveCard;
    }


    @Override
    public void updatePlayerScore(String playerNickname, int newPlayerScore) {
        this.playersScore.computeIfPresent(playerNickname, (key, oldValue) -> newPlayerScore);
        this.playersScore.putIfAbsent(playerNickname, newPlayerScore);
    }









    //TODO: ANCORA DA FARE --------------------------------------------------------------------------------------------------
    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn) {
       /* String message = playerNickname + " positioned " + serialCardPlaced + " on " + (isFlipped ? "back" : "front") + " in: " + x + ", " + y + " on turn: " + turn;
        if (playerNickname.equals(this.nickname)) {
            if (this.turnPlayed >= 0) {
                System.out.println(message);
                //this.printer.comeBack(this);
            } else {
                System.out.println(message + ".\nWaiting for other players...");
            }
        }
        if (!this.playerPositions.containsKey(playerNickname)) {
            this.playersBoard.put(playerNickname, new BoardView());
        }
        this.gamesLog.add(message);
        this.playersBoard.get(playerNickname).insertCard(y, x, serialCardPlaced, turn, isFlipped);*/
    }




    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {
        /*if (playerNickname.equals(this.nickname)) {
            this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", privateObjectiveCards);
            try {
                while (!privateObjectiveCards.contains(choice)) {
                    System.out.print("Choose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]: ");
                    this.choice = Integer.parseInt(this.reader.readLine());
                }
                this.virtualServer.choosePrivateObjectiveCard(choice);
                this.choice = 0;
                //System.out.println("++Sent private objective card choice message");
            } catch (NumberFormatException | IOException e) {
                System.out.print("Error: Please put a number.\nChoose your private objective card [" + privateObjectiveCards.stream().map(Object::toString).collect(Collectors.joining("] [")) + "]: ");
            }
        }*/
    }


    @Override
    public void setPrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers) {
       /* if (playerNickname.equals(this.nickname)) {
            this.turnPlayed++;
            this.serialPrivateObjectiveCard = serialPrivateObjectiveCard;
            String message = "Your private objective card is " + serialPrivateObjectiveCard;
            System.out.println(message + ".");
            this.gamesLog.add(message);
        } else {
            this.gamesLog.add(playerNickname + " choose private objective card");
        }
        if (this.serialPrivateObjectiveCard != 0 && readyPlayers != neededPlayers) {
            System.out.println("--|players that chose objective card: " + readyPlayers + "/" + neededPlayers);
        }*/
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
    public void showHomeMenu() {
      /*  System.out.println("\n--- HOME MENU ---");
        if (this.myTurn) {
            System.out.println("It's your turn");
        } else {
            System.out.println("It's not your turn");
        }
        System.out.println("\t[1] to view your hand");
        System.out.println("\t[2] to place a card (only when in turn)");
        System.out.println("\t[3] to view common objective card");
        System.out.println("\t[4] to view your private objective card");
        System.out.println("\t[5] to view your board");
        System.out.println("\t[6] to view other players' board");
        System.out.println("\t[7] to send a message in the chat");
        System.out.println("\t[8] to show game's history");
        System.out.println("\t[9] to show players' turns");
        System.out.println("\t[10] to draw card (only when in turn)");

        try {
            do {
                System.out.print("Your choice: ");
                this.choice = Integer.parseInt(reader.readLine());
                System.out.println("SELECTION: " + this.choice);
                if (this.choice < 1 || this.choice > 10) {
                    System.out.println("Invalid choice.");
                }
            } while (this.choice < 1 || this.choice > 10);

        } catch (IOException e) {
            System.out.print("Error: Please put a number: ");
        }
        switch (this.choice) {
            case 1: {
                this.printer.showHand(this.hand);
                this.printer.comeBack(this);
                break;
            }
            case 2: {
                this.placeCard();
                // this.printer.comeBack(this); in method OnPlaceCard()
                break;
            }
            case 3: {
                this.printer.showObjectiveCard("\n--- COMMON OBJECTIVE CARDS ---", this.serialCommonObjectiveCard);
                this.printer.comeBack(this);
                break;
            }
            case 4: {
                this.printer.showObjectiveCard("\n--- PRIVATE OBJECTIVE CARD ---", List.of(this.serialPrivateObjectiveCard));
                this.printer.comeBack(this);
                break;
            }
            case 5: {
                this.playersBoard.get(this.nickname).printBoard();
                this.printer.comeBack(this);
                break;
            }
            case 6: {
                try {
                    String playerChosen;
                    do {
                        System.out.println("Choose player board to view: [" + String.join("], [", this.playersBoard.keySet()) + "]");
                        System.out.print("Player: ");
                        playerChosen = reader.readLine();
                        while (!this.playersBoard.containsKey(playerChosen)) {
                            System.out.print("Player " + playerChosen + " not found.\nEnter an existing player: ");
                            playerChosen = reader.readLine();
                        }
                    } while (!this.playersBoard.containsKey(playerChosen));
                    System.out.println("Player chosen: " + playerChosen);
                    this.playersBoard.get(playerChosen).printBoard();
                } catch (IOException e) {
                    System.out.println("Error: Please put a number");
                }
                this.printer.comeBack(this);
                break;
            }
            case 7: {
                System.out.println("To do");
                this.printer.comeBack(this);
                break;
            }
            case 8: {
                this.printer.showHistory(this.gamesLog);
                this.printer.comeBack(this);
                break;
            }
            case 9: {
                System.out.println("Player's position are: ");
                this.playerPositions.forEach((key, value) -> System.out.println(key + ": " + value));
                // test -> this.playerPositions.forEach((key, value) -> System.out.print(String.join("\n", key + ": " + value)));
                this.printer.comeBack(this);
                break;
            }
            case 10:
                this.drawCard();
                break;
        }
        this.choice = 0;*/
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
    public void gameOver(String winner) {

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


    @Override
    public void updateTurn(String playerNickname, boolean turn) {
      /*  if (playerNickname.equals(this.nickname)) {
            this.myTurn = turn;
            if (this.myTurn) {
                this.showHomeMenu();
            } else if (this.turnPlayed == 0) {
                this.showHomeMenu();
            }
        }
        if (turn) {
            this.gamesLog.add("\nIt's " + playerNickname + "'s turn");
        } else {
            this.gamesLog.add("\n" + playerNickname + " passed the turn");
        }*/
    }
}
