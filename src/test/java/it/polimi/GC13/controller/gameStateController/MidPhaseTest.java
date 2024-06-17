package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.model.Game;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.TUI.BoardView;
import it.polimi.GC13.view.TUI.Printer;
import junit.framework.TestCase;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class MidPhaseTest extends TestCase {

    Game game = new Game(2, "test");
    Player player1 = new Player("player1");
    Player player2 = new Player("player2");
    MidPhase midPhase;
    SetupPhase setupPhase;
    BoardView boardView = new BoardView();


    private void createGame() {
        try {
            this.game.addPlayerToGame(this.player1);
            this.game.addPlayerToGame(this.player2);
            this.setupPhase = new SetupPhase(new Controller(this.game, null, null));
        } catch (Exception e) {
            fail("Error creating");
        }
    }

    private MidPhase createPhase() {
        Controller controller = new Controller(game,null,null);
        midPhase = new MidPhase(controller);
        createGame();
        boardView.insertCard(50, 50, player1.getHand().getFirst().serialNumber, 0, true);
        setupPhase.placeStartCard(player1, true);
        setupPhase.placeStartCard(player2, true);
        return midPhase;
    }

    public void testPlaceCard() {
        this.midPhase = new MidPhase(new Controller(this.game, null, null));
        createGame();
        this.boardView.insertCard(50, 50, this.player1.getHand().getFirst().serialNumber, 0, true);
        this.setupPhase.placeStartCard(this.player1, true);
        this.setupPhase.placeStartCard(this.player2, true);

        int X = 49;
        int Y = 49;
        this.player1.setMyTurn(true);
        this.player2.setMyTurn(true);

        // player 1 start card and first resource card
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, X, Y);
        this.boardView.insertCard(Y, X, this.player1.getHand().getFirst().serialNumber, 1, true);
        assert (this.player1.getBoard().checkListContainsCoordinates(this.player1.getBoard().getBoardMap().keySet(), new Coordinates(X, Y)));
        assert (this.player1.getHand().size() == 2);
        this.boardView.printBoard();

        this.midPhase.placeCard(this.player2, this.player2.getHand().getFirst().serialNumber, true, X, Y);
        assert (this.player2.getBoard().checkListContainsCoordinates(this.player1.getBoard().getBoardMap().keySet(), new Coordinates(X, Y)));
        assert (this.player2.getHand().size() == 2);

        X += 5;
        Y -= 1;
        // player 1 second resource card
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, X, Y);
        //this.boardView.insertCard(Y, X, this.player1.getHand().getFirst().serialNumber, 2, true);
        assert (!this.player1.getBoard().checkListContainsCoordinates(this.player1.getBoard().getBoardMap().keySet(), new Coordinates(X, Y)));
        assert (this.player1.getBoard().getBoardMap().size() == 2);

        this.boardView.printBoard();
    }

    public void testDrawCard() {
        createGame();

        // insert start card in player1 board
        this.boardView.insertCard(50, 50, this.player1.getHand().getFirst().serialNumber, 0, true);
        this.setupPhase.placeStartCard(this.player1, true);
        this.setupPhase.placeStartCard(this.player2, true);

        // check that both players have 3 cards in each hand
        assert (this.player1.getHand().size() == 3);
        assert (this.player2.getHand().size() == 3);

        // set players position
        this.player1.setPosition(Position.FIRST);
        this.player2.setPosition(Position.SECOND);
        this.midPhase = new MidPhase(new Controller(this.game, null, null));

        // place first card
        PlayableCard cardToPlace = this.player1.getHand().getFirst();
        this.midPhase.placeCard(this.player1, this.player1.getHand().getFirst().serialNumber, true, 49, 49);
        this.boardView.insertCard(49, 49, this.player1.getHand().getFirst().serialNumber, 1, true);

        // check that the card has been placed and the player doesn't have in his hand
        assert (this.player1.getBoard().checkListContainsCoordinates(this.player1.getBoard().getBoardMap().keySet(), new Coordinates(49, 49)));
        assert (this.player1.getHand().size() == 2);
        assert (!this.player1.getHand().contains(cardToPlace));

        assert (this.game.getTable().getResourceCardMap().size() == 3);
        assert (this.game.getTable().getGoldCardMap().size() == 3);

        boolean flag = true;
        PlayableCard cardToDraw;

        if (flag) {
            Printer printer = new Printer();
            Map<PlayableCard, Boolean> cardToDrawMap = this.player1.getTable().getResourceCardMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));

            printer.showDrawableCards(this.player1.getTable().getCardSerialMap(cardToDrawMap));

            cardToDrawMap.clear();

            cardToDrawMap = this.player1.getTable().getResourceCardMap().entrySet().stream()
                    .filter(entry -> !entry.getValue())
                    .findFirst()
                    .stream()
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));


            cardToDraw = cardToDrawMap.keySet().stream().findFirst().orElseThrow();

            System.out.println("Test serial card to draw: " + cardToDraw.serialNumber);
            assert (this.game.getTable().getResourceCardMap().containsKey(cardToDraw));
            assert (this.player1.isMyTurn());

            // METHOD TO CHECK
            this.midPhase.drawCard(player1, cardToDraw.serialNumber);

            // check that the player has the selected card in his hand and that hand size is 3
            assert (this.player1.getHand().contains(cardToDraw));
            assert (this.player1.getHand().size() == 3);

            assert (!this.player1.getTable().getResourceCardMap().containsKey(cardToDraw));
            assert (this.game.getTable().getGoldCardMap().size() == 3);
            assert (this.game.getTable().getResourceCardMap().size() == 3);

            cardToDrawMap.clear();
            cardToDrawMap = this.player1.getTable().getResourceCardMap().entrySet().stream().collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue));
            printer.showDrawableCards(this.player1.getTable().getCardSerialMap(cardToDrawMap));

        } else {
            cardToDraw = this.player1.getTable().getGoldCardMap().keySet().stream().findAny().orElseThrow();

            System.out.println("Test card to draw: " + cardToDraw.serialNumber);
            assert (this.game.getTable().getGoldCardMap().containsKey(cardToDraw));

            // METHOD TO CHECK
            assert (this.player1.isMyTurn());
            this.midPhase.drawCard(player1, cardToDraw.serialNumber);

            // check that the player has the selected card in his hand and that hand size is 3
            assert (this.player1.getHand().contains(cardToDraw));
            assert (this.player1.getHand().size() == 3);

            assert (!this.player1.getTable().getGoldCardMap().containsKey(cardToDraw));
            assert (this.game.getTable().getResourceCardMap().size() == 3);
            assert (this.game.getTable().getGoldCardMap().size() == 3);
        }
        assert (!this.player1.isMyTurn());
    }

    public void testCardPoints()  {
        this.midPhase = new MidPhase(new Controller(this.game, null, null));
        createGame();
        this.boardView.insertCard(50, 50, this.player1.getHand().getFirst().serialNumber, 0, true);
        this.setupPhase.placeStartCard(this.player1, true);
        this.setupPhase.placeStartCard(this.player2, true);

        PlayableCard cardToPlace = this.player1.getTable().getDeck().getCard(18);
        try {
            this.player1.addToHand(List.of(cardToPlace));
        } catch (GenericException e) {
            fail(e.getMessage());
        }

        this.player1.setMyTurn(true);
        assert (this.player1.getTable().getPlayersScore().get(this.player1) == 0);
        this.midPhase.placeCard(this.player1, cardToPlace.serialNumber, false, 51, 51);
        assert (!this.player1.getHand().contains(cardToPlace));
        assert (this.player1.getTable().getPlayersScore().get(this.player1) == 1);
    }

    public void testDrawCardOnLastRound() {
        midPhase = new MidPhase(new Controller(game, null, null));
        createGame();
        boardView.insertCard(50, 50, player1.getHand().getFirst().serialNumber, 0, true);
        setupPhase.placeStartCard(player1, true);
        setupPhase.placeStartCard(player2, true);

        //player1 sets last round = 1
        System.out.println("========TEST: setting last turn to: " + (game.getLastRound()+1));
        game.setLastRound(player1);
        assert (game.getLastRound() == 1);
        //player1 has played 1 round
        System.out.println("========TEST: increasing player1's turn to: " + (player1.getTurnPlayed()+1));
        player1.increaseTurnPlayed();
        assert (player1.getTurnPlayed() == 1);
        Map<Integer, Boolean> serialMap = game.getTable().getCardSerialMap(game.getTable().getResourceCardMap());
        LinkedList<Integer> cardsOnTable = new LinkedList<>();
        serialMap.forEach((key, value) -> cardsOnTable.add(key));
        System.out.println("========TEST: making player2 draw, LastRound : "+game.getLastRound()+", TurnPlayed : " + player2.getTurnPlayed());
        player2.setMyTurn(true);
        midPhase.drawCard(player2, cardsOnTable.getFirst());
        System.out.println("========TEST: increasing player2's turn");
        player2.increaseTurnPlayed();
        assert (player2.getTurnPlayed() == 1);

        System.out.println("========TEST: making player1 draw, LastRound : "+game.getLastRound()+", TurnPlayed : " + player1.getTurnPlayed());
        midPhase.drawCard(player1, cardsOnTable.get(1)); //no need to increase the turn because p1 is 'ahead' by 1 turn
        System.out.println("========TEST: making player2 draw, LastRound : "+game.getLastRound()+", TurnPlayed : " + player2.getTurnPlayed());
        midPhase.drawCard(player2, cardsOnTable.get(2));
    }


    public void testAddPlayerToExistingGame() {
        midPhase = createPhase();

        Player player3 = new Player("player3");
        midPhase.addPlayerToExistingGame(player3,game,null);
        assertFalse(game.getPlayerList().contains(player3));
        assertTrue(game.getPlayerList().contains(player2));
        assertTrue(game.getPlayerList().contains(player1));
    }

    public void testNewChatMessage() {
        //the validity of recipient and sender is managed by the view
        midPhase = createPhase();

        midPhase.newChatMessage("a","b","test");
        midPhase.newChatMessage("b","a","test");

        midPhase.newChatMessage("a","a","test");
        midPhase.newChatMessage("a","global","test");
    }
}