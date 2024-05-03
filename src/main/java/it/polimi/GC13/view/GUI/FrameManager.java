package it.polimi.GC13.view.GUI;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.model.ObjectiveCard;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.model.StartCard;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnInputExceptionMessage;
import it.polimi.GC13.view.GUI.game.MainPage;
import it.polimi.GC13.view.GUI.login.LoginFrame;
import it.polimi.GC13.view.View;

import javax.swing.*;
import java.io.IOException;
import java.util.List;
import java.util.Map;



//da provare a gestire i vari frame da qui
/*public class FrameManager extends JFrame implements View {
    ServerInterface virtualServer;
    private int choice = -1;



    public FrameManager(ServerInterface virtualServer) {
        this.virtualServer = virtualServer;
        this.checkForExistingGame();
    }


    @Override
    public void tokenSetupPhase(int readyPlayers, List<TokenColor> tokenColorList, int neededPlayers) {

    }

    @Override
    public void handUpdate(String playerNickname, int[] availableCard) {

    }

    @Override
    public void setSerialCommonObjectiveCard(List<Integer> serialCommonObjectiveCard) {

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
    *

        if (gameNameWaitingPlayersMap.isEmpty()) {
            //rimanda a LoginFrame: CASO 1)
            try {
                createNewGame();
            } catch (IOException e) {
                JOptionPane.showMessageDialog(null, "Error while creating the game.", "Creation Failed", JOptionPane.WARNING_MESSAGE);
            }
        } else {
            //ask what the player wants to do
            while(choice == -1){
                Object[] options = {"Create Game", "Join Game"};
                choice = JOptionPane.showOptionDialog(null, "There are existing games, choose: ", "Create Game / Join Game", JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            }

            if (this.choice == JOptionPane.YES_OPTION) {
                //player wants to create a new game
                //rimanda a LoginFrame: CASO 2a)
                try {
                    createNewGame();
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while creating the game.", "Creation Failed", JOptionPane.WARNING_MESSAGE);
                }
            } else if(this.choice == JOptionPane.NO_OPTION) {
                //player can and wants to join an existing game
                //rimanda a LoginFrame: CASO 2b)
                try {
                    joinExistingGame(gameNameWaitingPlayersMap);
                } catch (IOException e) {
                    JOptionPane.showMessageDialog(null, "Error while joining game.", "Joining Failed", JOptionPane.WARNING_MESSAGE);
                }
            }
        }
        this.choice = -1;
    }



    private void createNewGame() throws IOException {
        /* create and send message for a new game *

        boolean b = true;
       // SwingUtilities.invokeLater(() -> new LoginFrame(b));







        System.out.print("Choose a name for the new Game: ");


        System.out.print("Choose Number of players in the game [min 2, max 4]: ");
        do {
            try {
                playersNumber = Integer.parseInt(this.reader.readLine());
            } catch (NumberFormatException e) {
                System.out.print("Error: Please put a number: ");
            }
            if (playersNumber < 2 || playersNumber > 4) {
                System.out.print("Error: Please choose a number between 2 and 4: ");
            }
        } while (playersNumber < 2 || playersNumber > 4);

        //massage is ready to be sent
        virtualServer.addPlayerToGame(this.player, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }


    public void joinExistingGame(Map<String, Integer> gameNameWaitingPlayersMap) throws IOException {

        boolean b = false;
        SwingUtilities.invokeLater(() -> new LoginFrame(b));



        String nickname, gameName;
        int playersNumber = -1;

        System.out.print("Choose a nickname: ");
        nickname = this.reader.readLine();
        this.player = new Player(nickname);

        System.out.println("Joinable Games:");
        gameNameWaitingPlayersMap.forEach((string, numCurrPlayer) -> System.out.println("\t>game:[" + string + "] --|players in waiting room: " + numCurrPlayer +"|"));
        do {
            System.out.print("Select the game to join using its name: ");
            gameName = this.reader.readLine();
        } while (!gameNameWaitingPlayersMap.containsKey(gameName));

        //massage is ready to be sent
        virtualServer.addPlayerToGame(player, playersNumber, gameName);
        System.out.println("++Sent: addPlayerToGame");
    }





    @Override
    public void startCardSetupPhase(String playerNickname, TokenColor tokenColor) {

    }

    @Override
    public void chosePrivateObjectiveCard(String playerNickname, int[] privateObjectiveCard) {

    }

    @Override
    public void onPositionedCard(String playerNickname, int startCardPlaced, boolean isFlipped) {

    }

    @Override
    public void definePrivateObjectiveCard(String playerNickname, int indexPrivateObjectiveCard) {

    }

    @Override
    public void exceptionHandler(String playerNickname, OnInputExceptionMessage onInputExceptionMessage) {

    }

    private void showObjectiveCard(List<Integer> serialNumber) {
        for (ObjectiveCard card : visualdeck.getObjectiveDeck()) {
            if (card.serialNumber == serialNumber.getFirst() || card.serialNumber == serialNumber.getLast()) {
                System.out.println(this.player.getNickname() + " privateObjectiveCard: ");
                card.printObjectiveCard();
            }
        }
    }

    private void showHand() {
        if (this.hand.getFirst() < 81 || this.hand.getFirst() > 86) {
            for (PlayableCard card : visualdeck.getResourceDeck()) {
                for (int x : this.hand) {
                    if (card.serialNumber == x) {
                        card.cardPrinter(false);
                    }
                }
            }
        } else {
            for (StartCard card : visualdeck.getStartDeck()) {
                for (int x : this.hand) {
                    if (card.serialNumber == x) {
                        card.cardPrinter(false);
                        System.out.println("       FRONT");
                        card.cardPrinter(true);
                        System.out.println("        BACK");
                    }
                }
            }
        }
    }
}*/
