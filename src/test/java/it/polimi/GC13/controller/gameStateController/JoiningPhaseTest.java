package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.app.ConnectionBuilder;
import it.polimi.GC13.controller.LobbyController;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.rmi.RMIConnectionAdapter;
import it.polimi.GC13.network.socket.ClientDispatcher;
import junit.framework.TestCase;

import java.rmi.RemoteException;

public class JoiningPhaseTest extends TestCase {
    Game game = new Game(2, "test");
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    JoiningPhase joinPhase;

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

    public void testAddPlayerToExistingGame() {
        LobbyController lobbyController = new LobbyController();
        Controller controller = new Controller(this.game, lobbyController, null);

        ClientInterface client = createClient();
        this.joinPhase = new JoiningPhase(controller);

        try {
            //check for duplicate nickname
            game.addPlayerToGame(player1);
            joinPhase.addPlayerToExistingGame(new Player(player1.getNickname()), game, client);
            fail("exception should have been thrown");
        } catch (GenericException e) {
           System.out.println(e.getMessage());
        }
        Game game1 = new Game(2, "secondGame");
        try {
            //check for exception on already full game
            joinPhase.addPlayerToExistingGame(this.player1, game1, client);
            joinPhase.addPlayerToExistingGame(this.player2, game1, client);
            assert (game1.getPlayerList().size() == 2);
            joinPhase.addPlayerToExistingGame(new Player(player1.getNickname()), game1, client);
            fail("exception should have been thrown");
        } catch (GenericException e) {
           System.out.println(e.getMessage());
        }
    }
}
