package it.polimi.GC13.controller;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.controller.gameStateController.Controller;
import it.polimi.GC13.controller.gameStateController.DealingPhase;
import it.polimi.GC13.controller.gameStateController.GamePhase;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.ObjectiveCard;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.rmi.RMIConnectionAdapter;
import it.polimi.GC13.network.socket.ClientDispatcher;
import junit.framework.TestCase;

import java.rmi.RemoteException;

public class LobbyControllerTest extends TestCase {

    LobbyController lobbyController = new LobbyController();
    ControllerDispatcher controllerDispatcher = new ControllerDispatcher(lobbyController);

    private ClientInterface setUpClient() {
        try {
            RMIConnectionAdapter client = new RMIConnectionAdapter(new ClientDispatcher());
            client.connectionBuilderSetup(new ConnectionBuilder(0, 0, 0, 0));
            return client;
        } catch (RemoteException e) {
            fail("failed to create client \n" + e.getMessage());
            return null;
        }
    }

    private ClientInterface setUpLobbyController() {
        lobbyController.setControllerDispatcher(controllerDispatcher);
        return setUpClient();
    }

    private void simulateMidGame() {
        Game game = new Game(2, "g1");
        Controller controller = new Controller(game, null, null);
        GamePhase gamePhase = new DealingPhase(controller);
        Player player1 = new Player("p1");
        Player player2 = new Player("p1");
        try {
            game.addPlayerToGame(player1);
            game.addPlayerToGame(player2);

            player1.setPosition(Position.FIRST);
            player2.setPosition(Position.SECOND);

            game.dealPrivateObjectiveCards();
            ObjectiveCard objectiveCardP1 = player1.getPrivateObjectiveCard().getFirst();
            ObjectiveCard objectiveCardP2 = player2.getPrivateObjectiveCard().get(1);

            gamePhase.choosePrivateObjective(player1, objectiveCardP1.serialNumber);
            gamePhase.choosePrivateObjective(player2, objectiveCardP2.serialNumber);
        } catch (GenericException e) {
            fail(e.getMessage());
        }
    }

    public void testAddPlayerToGame() {
        ClientInterface client = setUpLobbyController();

        try {
            //test correct join
            lobbyController.createNewGame(client, "playerName1", 3, "gameName1");
            //game doesn't start after one player joins
            lobbyController.addPlayerToGame(client, "playerName2", "gameName1");
            assert (!lobbyController.getStartedGameMap().containsKey("gameName1"));
            assert (lobbyController.getJoinableGameMap().containsKey("gameName1"));

            //lobbyC doesn't put players in incorrect game
            lobbyController.createNewGame(client, "playerNameA", 4, "gameName2");
            lobbyController.addPlayerToGame(client, "playerName2", "gameName2");
            assert (!lobbyController.getStartedGameMap().containsKey("gameName1"));
            assert (lobbyController.getJoinableGameMap().containsKey("gameName1"));

            //game starts when number of players is correct
            lobbyController.addPlayerToGame(client, "playerName3", "gameName1");
            assert (!lobbyController.getJoinableGameMap().containsKey("gameName1"));
            assert (lobbyController.getStartedGameMap().containsKey("gameName1"));
        } catch (RemoteException e) {
            fail(e.getMessage());
        }

    }

    public void testCreateNewGame() {
        ClientInterface client = setUpLobbyController();
        assert (lobbyController.getStartedGameMap().isEmpty());
        assert (lobbyController.getJoinableGameMap().isEmpty());
        try {
            lobbyController.createNewGame(client, "playerName1", 2, "gameName1");
            assert (lobbyController.getJoinableGameMap().containsKey("gameName1"));
            assert (!lobbyController.getStartedGameMap().containsKey("gameName1"));
            assert (!lobbyController.getJoinableGameMap().containsKey("playerName1"));
        } catch (RemoteException e) {
            fail("exception when creating a new game with empty lobbyController should not be thrown");
        }
        try {
            lobbyController.createNewGame(client, "playerName1", 2, "gameName1");
        } catch (RemoteException e) {
            fail("exception when creating a new game should not be thrown");
        }
    }

    public void testReconnectPlayerToGame() {
        simulateMidGame();

        ClientInterface client = setUpLobbyController();
        try {
            //first player tries to reconnect
            lobbyController.reconnectPlayerToGame(client, "g1", "p1");
            //player tries to reconnect to an invalid game
            lobbyController.reconnectPlayerToGame(client, "nonExistingGame", "invalidPlayer");
            //second player tries to reconnect
            lobbyController.reconnectPlayerToGame(client, "g1", "p2");
            //first player tries to reconnect again
            lobbyController.reconnectPlayerToGame(client, "g1", "p1");
        } catch (RemoteException e) {
            fail(e.getMessage());
        }
    }

    public void testCloseGame() {
        ClientInterface client = setUpLobbyController();
        ClientInterface client2 = setUpClient();
        simulateMidGame();
        try {
            lobbyController.createNewGame(client, "p1", 2, "g1");
            lobbyController.addPlayerToGame(client2, "p2", "g1");
            lobbyController.closeGame(client);
            assert(!lobbyController.getStartedGameMap().containsKey("g1"));
            assert(!lobbyController.getJoinableGameMap().containsKey("g1"));
        } catch (RemoteException e) {
            fail(e.getMessage());
        }

    }
}