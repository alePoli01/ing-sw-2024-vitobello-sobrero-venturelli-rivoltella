package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.rmi.RMIConnectionAdapter;
import it.polimi.GC13.network.socket.ClientDispatcher;
import junit.framework.TestCase;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.Map;

public class ControllerTest extends TestCase {
    /*
    test methods that can't be used during a specific phase
     */
    Game game = new Game(2, "gameName");
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    Deck deck = new Deck();

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public void testJoiningPhaseErrorMethods() {
        JoiningPhase joiningPhase = new JoiningPhase(new Controller(game, null, null));
        String[] message = {
                "Error, game is in JOINING phase.",
                "Error, game is in JOINING phase.",
                "Error, game is in JOINING phase.",
                "Error, game is in JOINING phase.",
                "Error, game is in JOINING phase.",
                "Error, game is in JOINING phase."
        };
        try {
            joiningPhase.chooseToken(null, null);
            joiningPhase.choosePrivateObjective(null, 0);
            joiningPhase.placeStartCard(null, false);
            joiningPhase.placeCard(null, 0, false, 0, 0);
            joiningPhase.drawCard(null, 0);
            joiningPhase.newChatMessage(null, null, null);
            String[] messageOut = outContent.toString().trim().split("\\r?\\n");
            for (int i = 0; i < message.length; i++) {
                assertEquals(message[i], messageOut[i]);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testSetupPhaseErrorMethods() {
        System.setOut(originalOut);
        LobbyController lobbyController = new LobbyController();
        Controller controller = new Controller(game, lobbyController, null);
        GamePhase gamePhase = new JoiningPhase(controller);
        ClientInterface client = createClient();
        try {
            game.addPlayerToGame(player2);
            gamePhase.addPlayerToExistingGame(player1,game,client);

        } catch (GenericException e) {
            fail(e.getMessage());
        }
        System.setOut(new PrintStream(outContent));

        String[] message = {
                "Error, game is in SETUP phase.",
                "Error, game is in SETUP phase.",
                "Error, game is in SETUP phase.",
                "Error, game is in SETUP phase.",
        };
        try {

            controller.choosePrivateObjective(null, 0);
            controller.placeCard(null, 0, false, 0, 0);
            controller.drawCard(null, 0);
            controller.newChatMessage(null, null, null);
            String[] messageOut = outContent.toString().trim().split("\\r?\\n");
            for (int i = 0; i < message.length; i++) {
                assertEquals(message[i], messageOut[i]);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }

        //test addPlayerToExistingGame
        try {
            Player player3 = new Player("player3");
            controller.addPlayerToExistingGame(player3,game,null);
            assertFalse(game.getPlayerList().contains(player3));
            assertTrue(game.getPlayerList().contains(player2));
            assertTrue(game.getPlayerList().contains(player1));
        } catch (GenericException e) {
            fail("addPlayerToExistingGame should not throw Exception");
        }
    }

    public void testDealingPhaseErrorMethods() {
        System.setOut(originalOut);
        Controller controller = new Controller(game, null, null);
        GamePhase gamePhase = new SetupPhase(controller);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);
            game.dealStartCard();


            gamePhase.placeStartCard(player1, true);
            gamePhase.placeStartCard(player2, true);

        } catch (GenericException e) {
            fail(e.getMessage());
        }
        System.setOut(new PrintStream(outContent));

        String[] message = {
                "Error, game is in DEALING_CARDS phase.",
                "Error, game is in DEALING_CARDS phase.",
                "Error, game is in DEALING_CARDS phase.",
                "Error, game is in DEALING_CARDS phase.",
                "Error, game is in DEALING_CARDS phase.",
                "Error, game is in DEALING_CARDS phase."
        };
        try {
            controller.chooseToken(null, null);
            controller.placeStartCard(null, false);
            controller.placeCard(null, 0, false, 0, 0);
            controller.drawCard(null, 0);
            controller.newChatMessage(null, null, null);
            controller.addPlayerToExistingGame(null, null, null);
            String[] messageOut = outContent.toString().trim().split("\\r?\\n");
            for (int i = 0; i < message.length; i++) {
                assertEquals(message[i], messageOut[i]);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    public void testMidPhaseErrorMethods() {
        System.setOut(originalOut);
        Controller controller = new Controller(game, null, null);
        GamePhase gamePhase = new DealingPhase(controller);
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);

            player1.setPosition(Position.FIRST);
            player2.setPosition(Position.SECOND);

            game.dealPrivateObjectiveCards();
            gamePhase.choosePrivateObjective(player1, player1.getPrivateObjectiveCard().getFirst().serialNumber);
            gamePhase.choosePrivateObjective(player2, player2.getPrivateObjectiveCard().getFirst().serialNumber);
        } catch (GenericException e) {
           fail(e.getMessage());
        }
        System.setOut(new PrintStream(outContent));
        String[] message = {
                "Error, game is in MID phase.",
                "Error, game is in MID phase.",
                "Error, game is in MID phase."
        };
        try {
            controller.chooseToken(null, null);
            controller.placeStartCard(null, false);
            controller.choosePrivateObjective(null, 0);
            String[] messageOut = outContent.toString().trim().split("\\r?\\n");
            for (int i = 0; i < message.length; i++) {
                assertEquals(message[i], messageOut[i]);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
    }

    private ClientInterface createClient() {
        try {
            RMIConnectionAdapter client = new RMIConnectionAdapter(new ClientDispatcher());
            client.connectionBuilderSetup(new ConnectionBuilder(0, 0, 0, 0));
            return client;
        } catch (RemoteException e) {
            fail("failed to create client");
            return null;
        }
    }

    public void testEndPhaseErrorMethods() {
        System.setOut(originalOut);
        deck.parseJSON();
        game.getTable().tableSetup();

        LobbyController lobbyController = new LobbyController();
        Controller controller = new Controller(this.game, lobbyController, null);
        GamePhase gamePhase = new MidPhase(controller);

        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        try {
            //add players to game
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);
            //place starting card
             System.out.println("\t\tsetting positions ");
                       player1.setPosition(Position.FIRST);
                       player2.setPosition(Position.SECOND);
            StartCard card = deck.getStartDeck().getFirst();
            player1.getBoard().placeCardToTheBoard(new Coordinates(50, 50), card, true);
            player2.getBoard().placeCardToTheBoard(new Coordinates(50, 50), card, true);
            //deal private objective cards
            game.dealPrivateObjectiveCards();
            player1.setPrivateObjectiveCard(player1.getPrivateObjectiveCard().getFirst().serialNumber, 1);
            player2.setPrivateObjectiveCard(player2.getPrivateObjectiveCard().getFirst().serialNumber, 2);
            //mid -> end
            PlayableCard pointCard = deck.getCard(39);
            //get card #39 : gives 1 point
            System.out.println("\t\tplayer1 playing card ");
            player1.getBoard().placeCardToTheBoard(new Coordinates(51, 51), pointCard, false);
            player1.getTable().setPlayerScore(player1, pointCard.getPointsGiven(player1.getBoard(), 51, 51));

            player1.setMyTurn(true);
            System.out.println("\t\tsetting last round ");
            game.setLastRound(player1);
            System.out.println("\t\tincreasing turns played");
            player1.increaseTurnPlayed();
            player2.increaseTurnPlayed();


            Map<Integer, Boolean> serialMap = game.getTable().getCardSerialMap(game.getTable().getResourceCardMap());
            LinkedList<Integer> cardsOnTable = new LinkedList<>();
            serialMap.forEach((key, value) -> cardsOnTable.add(key));
            gamePhase.drawCard(player1, cardsOnTable.getFirst());

        } catch (GenericException e) {
            fail(e.getMessage());
        }
        System.setOut(new PrintStream(outContent));

        String[] message = {
                "Error, game is in END phase.",
                "Error, game is in END phase.",
                "Error, game is in END phase.",
                "Error, game is in END phase.",
                "Error, game is in END phase.",
                "Error, game is in END phase.",
                "Error, game is in END phase."
        };
        try {
            controller.chooseToken(null, null);
            controller.choosePrivateObjective(null, 0);
            controller.placeStartCard(null, false);
            controller.placeCard(null, 0, false, 0, 0);
            controller.drawCard(null, 0);
            controller.newChatMessage(null, null, null);
            controller.addPlayerToExistingGame(null, null, null);
            String[] messageOut = outContent.toString().trim().split("\\r?\\n");
            for (int i = 0; i < message.length; i++) {
                assertEquals(message[i], messageOut[i]);
            }
        } catch (Exception e) {
            fail(e.getMessage());
        }
        try {
            outContent.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void setUp() throws Exception {
        super.setUp();
        System.setOut(new PrintStream(outContent));
    }

    public void tearDown() {
        System.setOut(originalOut);
    }
}