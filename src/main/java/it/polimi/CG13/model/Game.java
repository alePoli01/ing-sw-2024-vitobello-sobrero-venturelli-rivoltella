package it.polimi.CG13.model;

import it.polimi.CG13.enums.Color;
import it.polimi.CG13.enums.FirstCardsNotGivenException;
import it.polimi.CG13.enums.GameState;
import it.polimi.CG13.enums.Position;
import it.polimi.CG13.exception.CardNotAddedToHandException;
import it.polimi.CG13.exception.StartCardNotGivenException;

import java.util.List;
import java.util.ListIterator;
import java.util.Random;

public class Game {

    /*TODO: controllare chi usa GameState(controll o model?)
            capire se ci deve essere un attributo che rappresenta in quale GameState sono
    */
    private GameState gameState;
    private Deck deck;
    private Table table;
    private int numPlayer;
    private List<Player> playerList;
    private int round;

    public Game(GameState gameState, Deck deck, Table table, int numPlayer, List<Player> playerList, int round) {
        this.gameState = GameState.SETUP;
        this.deck = deck;
        this.table = table;
        this.numPlayer = numPlayer;
        this.playerList = playerList;
        this.round = round;
    }

    public GameState getGameState() {
        return gameState;
    }

    public void setGameState(GameState gameState) {
        this.gameState = gameState;
    }

    public Deck getDeck() {
        return deck;
    }

    public Table getTable() {
        return table;
    }

    public void randomizeArray() {
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    // give start card to each player in game setup phase
    public void giveStartCard() throws StartCardNotGivenException {
        for (Player player : this.playerList) {
            try {
                player.addToHand(deck.getStartDeck().getFirst());
            } catch (CardNotAddedToHandException e) {
                throw new StartCardNotGivenException(player);
            }
        }
    }

    // give firsts 3 cards to each player
    public void giveFirstCards() throws FirstCardsNotGivenException {
        for (Player player : this.playerList) {
            try {
                player.addToHand(deck.getResourceDeck().getFirst());
                player.addToHand(deck.getResourceDeck().getFirst());
                player.addToHand(deck.getGoldDeck().getFirst());
            } catch (CardNotAddedToHandException e) {
                throw new FirstCardsNotGivenException(player);
            }
        }
    }

    // add common objective card to the table
    public void setCommonObjectiveCard() {
        this.getTable().setCommonObjectiveCard(0, getDeck().objectiveDeck.getFirst());
        this.getTable().setCommonObjectiveCard(1, getDeck().objectiveDeck.getFirst());
    }

    // set first player
    public void setPlayersPosition() {
        Random random = new Random();
        int index = random.nextInt(playerList.size());
        playerList.get(index).setPosition(Position.PRIMO);
        playerList.get(index).setToken(Color.BLACK);
    }
}