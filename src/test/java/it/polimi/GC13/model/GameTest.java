package it.polimi.GC13.model;

import it.polimi.GC13.enums.CardType;
import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.ReignType;
import junit.framework.TestCase;

import java.util.*;
public class GameTest extends TestCase {
    /**
     * TODO: completare testGiveStartCard: parseJson da risolvere
     */
    public void testAddPlayer() {
    }
    public void testParseJSON(){
    }

    public void testGiveStartCard() {
      /*  Board board=new Board();
        Board board2=new Board();
        Player player1=new Player("giocatore1",board);
        Player player2=new Player("giocatore2",board2);
        playerlist.add(player1);
        playerlist.add(player2);

        Deck deck=new Deck();
        Table table=new Table();
        List<Player> playerlist =new ArrayList<>();



        deck.parseJSON();
        assertNull(deck.getStartDeck().getFirst());

        Game game = new Game(GameState.START, deck, table, 1, playerlist, 0);
*/
    }

    public void testHandUpdate(){
        int[] zero = new int[4];
        Arrays.fill(zero, 0);
        ArrayList<ReignType> reigntypes=new ArrayList<>();
        ArrayList<ReignType> objtypes=new ArrayList<>();
        ArrayList<ReignType> types=new ArrayList<>();
        for(int i=0;i<4;i++){
            reigntypes.add(ReignType.ANIMAL);
        }
        assertEquals(ReignType.ANIMAL,reigntypes.get(0));
        //PlayableCard card=new PlayableCard(1234,zero,reigntypes.getFirst(), CardType.STARTER ,reigntypes,zero,)
    }

}