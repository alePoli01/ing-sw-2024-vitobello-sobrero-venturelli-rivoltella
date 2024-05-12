package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.game.TokenChoose;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.GUI.login.WaitingLobby;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.rmi.RemoteException;
import java.util.List;
import java.util.Map;


// TODO: da concatenare i frame --> da capire se ogni frame lancia il successivo o se serve questa classe per gestirli
//TODO: da cambiare la gestione degli errori

public class FrameManager implements View {
    protected ServerInterface virtualServer;
    private int choice = -1;
    private WaitingLobby waitingLobby;

    @Override
    public void startView() {
        this.checkForExistingGame();
        System.out.println("++Sent: checkForExistingGame");
    }

    @Override
    public void setVirtualServer(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
    }

    public ServerInterface getVirtualServer() {
        return this.virtualServer;
    }

    @Override
    public void checkForExistingGame() {
        this.virtualServer.checkForExistingGame();
    }

    @Override
    public void joiningPhase(Map<String, Integer> gameNameWaitingPlayersMap) {
    /*JOINING PHASE
       1)Not existing game ==> login player name + game name + number of player needed to start
       2)Existing game:
            2.a) Create new game: CASO 1
            2.b) Join existing game: login player name + choose existing game



            NOTA BENE: property() per gestire il movimento dei token --> binding con i punteggi dei giocatori
    */

       /* if (gameNameWaitingPlayersMap.isEmpty()) {
            SwingUtilities.invokeLater(() -> new LoginFrame(this));
        } else {
            SwingUtilities.invokeLater(() -> new LoginFrame(this, gameNameWaitingPlayersMap));
        }*/


        if (gameNameWaitingPlayersMap.isEmpty()) {
            createNewGame();
            //da capire come gestire eventuali errori
            //JOptionPane.showMessageDialog(null, "Error while creating the game.", "Creation Failed", JOptionPane.WARNING_MESSAGE);
        } else {
            //ask what the player wants to do
            //da testare


            Object[] options = {"Create Game", "Join Game"};
            choice = JOptionPane.showOptionDialog(null, "There are existing games, choose: ", "Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, null);

            if (this.choice == JOptionPane.YES_OPTION) {
                //player wants to create a new game
                createNewGame();

                //da capire come gestire eventuali errori
                //JOptionPane.showMessageDialog(null, "Error while creating the game.", "Creation Failed", JOptionPane.WARNING_MESSAGE);

            } else if (this.choice == JOptionPane.NO_OPTION) {
                //player can and wants to join an existing game
                joinExistingGame(gameNameWaitingPlayersMap);

                //da capire come gestire eventuali errori
                //JOptionPane.showMessageDialog(null, "Error while joining game.", "Joining Failed", JOptionPane.WARNING_MESSAGE);
            }
        }
        this.choice = -1;
    }


    //da testare
    public void createNewGame() {
        /* create and send message for a new game */
        SwingUtilities.invokeLater(() -> new LoginFrame(this));

    }

    //da testare
    public void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) {
        SwingUtilities.invokeLater(() -> new LoginFrame(this, gameNameWaitingPlayersMap));
    }

    @Override
    public void chooseTokenSetupPhase(int readyPlayers, int neededPlayers, List<TokenColor> tokenColorList) {
        SwingUtilities.invokeLater(() -> {
            if (readyPlayers < neededPlayers) {
                if (waitingLobby == null) {
                    waitingLobby = new WaitingLobby(readyPlayers, neededPlayers);
                } else {
                    waitingLobby.getJLabel().setText(readyPlayers + "/" + neededPlayers);
                }
                waitingLobby.setVisible(true);
            } else {
                if (waitingLobby != null) {
                    waitingLobby.dispose();
                }
                SwingUtilities.invokeLater(() -> new TokenChoose(this));
            }
        });
    }


    @Override
    public void handUpdate(String playerNickname, List<Integer> availableCard) {

    }

    @Override
    public void placeStartCardSetupPhase(String playerNickname, TokenColor tokenColor) {
        //SwingUtilities.invokeLater(() -> new MainPage(virtualServer));


    }


    @Override
    public void choosePrivateObjectiveCard(String playerNickname, List<Integer> privateObjectiveCards) {

    }

    @Override
    public void onPlacedCard(String playerNickname, int serialCardPlaced, boolean isFlipped, int x, int y, int turn) {

    }

    @Override
    public void setPrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard, int readyPlayers, int neededPlayers) {

    }

    @Override
    public void drawCard() {

    }

    @Override
    public void showHomeMenu() {

    }

    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {

    }

    @Override
    public void setPlayersOrder(Map<String, Position> playerPositions) {

    }


    @Override
    public void displayAvailableCells(List<Coordinates> availableCells) {

    }

    @Override
    public void connectionLost() {

    }

    @Override
    public void onSetLastTurn(String nickname, Position position) {

    }

    @Override
    public void placeCard() {

    }

    @Override
    public void updatePlayerScore(String playerNickname, int newPlayerScore) {

    }

    @Override
    public void onNewMessage(String sender, String receiver, String message) {

    }

    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {

    }

    @Override
    public void updateGoldCardsAvailableToDraw(Map<Integer, Boolean> goldCardSerial) {

    }

    @Override
    public void updateResourceCardsAvailableToDraw(Map<Integer, Boolean> resourceCardSerial) {

    }


    @Override
    public void updateTurn(String playerNickname, boolean turn) {

    }
}
