package it.polimi.GC13.model;

import it.polimi.GC13.exception.GenericException;
import junit.framework.TestCase;

public class TableTest extends TestCase {
    Table table =new Table(new Game(4, "test"));

    private void backupCards(Table table, PlayableCard[] resourceCards, PlayableCard[] goldCards) {
        //get first 3 cards of each deck
        for (int i = 0; i < 3; i++) {
            System.out.println("carte numero:" + (i + 1));
            System.out.println("\tresource\t#" + table.getDeck().getResourceDeck().get(i).serialNumber);
            System.out.println("\tgold\t\t#" + table.getDeck().getGoldDeck().get(i).serialNumber);
            resourceCards[i] = table.getDeck().getResourceDeck().get(i);
            goldCards[i] = table.getDeck().getGoldDeck().get(i);
        }
    }

    /*public void testTableSetup(){

        Table table =new Table(new Game(4, "test"));
        table. getDeck().shuffleDecks();
        PlayableCard[] resourceCards= new PlayableCard[3];
        PlayableCard[] goldCards= new PlayableCard[3];

        backupCards(table,resourceCards,goldCards);

        table.tableSetup();
        System.out.println("===========================");
        for (int i = 0; i < 2; i++) {
            System.out.println("carte numero:"+(i+1));
            System.out.println("\tresource\t#"+table.getResourceFacedUp()[i].serialNumber);
            System.out.println("\tgold\t\t#"+table.getGoldFacedUp()[i].serialNumber);
            assertSame(resourceCards[i],table.getResourceFacedUp()[i]);
            assertSame(goldCards[i],table.getGoldFacedUp()[i]);
        }
        System.out.println("carte numero: 3");
        System.out.println("\tresource\t#"+table.getResourceFacedDown().serialNumber);
        System.out.println("\tgold\t\t#"+table.getGoldFacedDown().serialNumber);
        assertSame(resourceCards[2],table.getResourceFacedDown());
        assertSame(goldCards[2],table.getGoldFacedDown());
    }*/

    /** after setting up the table(all 4 cards faced up and 2 cards faced down)
             remove one card at a time(3 resource then 3 gold)
             try to remove 2 more(1 resource and 1 gold)
     */
    public void testDrawCard() {

        table.getDeck().shuffleDecks();

        PlayableCard[] resourceCards = new PlayableCard[3];
        PlayableCard[] goldCards = new PlayableCard[3];

        backupCards(table, resourceCards, goldCards);
        table.tableSetup();

        //the fetched cards are on the table faced up now
        for (int i = 0; i < 3; i++) {
            try {
                table.drawCard(resourceCards[i]);
                table.drawCard(goldCards[i]);
            } catch (GenericException e1) {
                fail("card #" + resourceCards[i].serialNumber + " should be found on the table");
            }
        }
        //now there's no cards on the table
        try {
            table.drawCard(resourceCards[0]);
            fail("card #" + resourceCards[0].serialNumber + " should NOT be on the table");
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
        try {
            table.drawCard(goldCards[0]);
            fail("card #" + goldCards[0].serialNumber + " should NOT be on the table");
        } catch (GenericException e) {
            System.out.println(e.getMessage());
        }
    }

    /*
        TEST PASSED
     */
    public void testAddPlayerScore() throws GenericException {
        Game game = new Game(2, "test");
        Player player1 = new Player("player1");
        Player player2 = new Player("player2");
        game.addPlayerToGame(player1);
        game.addPlayerToGame(player2);

        game.getPlayerList().forEach(player -> game.getTable().addPlayerScore(player, 0));
        game.getTable().addPlayerScore(player1, 0);
        game.getTable().addPlayerScore(player2, 0);
        assert (game.getTable().getPlayersScore().get(player1) == 0);
        assert (game.getTable().getPlayersScore().get(player2) == 0);
        game.getTable().addPlayerScore(player1, 5);
        game.getTable().addPlayerScore(player2, 6);
        assert (game.getTable().getPlayersScore().get(player1) == 5);
        assert (game.getTable().getPlayersScore().get(player2) == 6);
        game.getTable().addPlayerScore(player1, 1);
        assert (game.getTable().getPlayersScore().get(player1) == 6);
        assert (game.getTable().getPlayersScore().get(player2) == 6);
    }

    /*public void testGetNewCard() {
        table.getDeck().shuffleDecks();
        PlayableCard resourceCard = table.getDeck().getResourceDeck().getFirst();
        PlayableCard goldCard = table.getDeck().getGoldDeck().getFirst();
        for (int i = 0; i < 3; i++) {
            try {
                table.getNewCard(resourceCard);
            } catch (GenericException e) {
                fail(e.getMessage());
            }
            try {
                table.getNewCard(goldCard);
            } catch (GenericException e) {
                fail(e.getMessage());
            }
        }
    }*/
    /*public void testGetNewCardWithEmptyDeck(){
        table.getDeck().shuffleDecks();
        PlayableCard resourceCard = table.getDeck().getResourceDeck().getFirst();
        for (int i = 0; i < 80; i++) {
            try {
                table.getNewCard(resourceCard);
            } catch (GenericException e) {
              fail(e.getMessage());
            }
        }
        try {
            table.getNewCard(resourceCard);
            fail("Exception wasn't thrown, there shouldn't be any card left in both decks");
        } catch (GenericException e) {
           e.getMessage();
        }

    }*/



}