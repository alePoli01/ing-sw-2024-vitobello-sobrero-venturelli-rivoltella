package it.polimi.GC13.model;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.inputException.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;

import java.io.Serializable;
import java.util.*;

public class Game implements Serializable {
    private GameState gameState;
    private final Deck deck;
    private final Table table;
    public final int numPlayer;
    private int currNumPlayer;
    private final List<Player> playerList;
    private int lastRound;

    public Game(int numPlayer) {
        this.gameState = GameState.JOINING;
        this.table = new Table();
        this.numPlayer = numPlayer;
        this.playerList = new ArrayList<>(){};
        this.deck = new Deck();
        this.currNumPlayer = 0;
    }

    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    public GameState getGameState() {
        return this.gameState;
    }

    public Deck getDeck() {
        return deck;
    }

    public Table getTable() {
        return table;
    }

    public int getCurrNumPlayer() {
        return currNumPlayer;
    }

    public int getNumPlayer() {
        return numPlayer;
    }

    public List<Player> getPlayerList() {
        return playerList;
    }

    // give firsts 3 cards and start card to each player
    public void giveFirstCards() throws CardNotAddedToHandException {
        for (Player player : this.playerList) {
            player.addToHand(deck.getResourceDeck().removeFirst());
            player.addToHand(deck.getResourceDeck().removeFirst());
            player.addToHand(deck.getGoldDeck().removeFirst());
        }
    }

    // add common objective card to the table
    public void setCommonObjectiveCards() {
        this.getTable().setCommonObjectiveCard(0, getDeck().getObjectiveDeck().removeFirst());
        this.getTable().setCommonObjectiveCard(1, getDeck().getObjectiveDeck().removeFirst());
    }

    // set players position
    public void setPlayersPosition() {
        Random random = new Random();
        int index = random.nextInt(playerList.size() - 1);
        int i = 0;
        for (Position p : Position.values()) {
            if (p.equals(Position.FIRST) || playerList.get(i).equals(playerList.get(index))) {
                this.playerList.get(index).setPosition(Position.FIRST);
            } else {
                this.playerList.get(i).setPosition(p);
            }
            i++;
        }
    }

    // updates players turn at the end of every round
    public void setPlayerTurn(Player player) {
        int index = this.playerList.indexOf(player);
        if (index == 3) {
            this.playerList.getFirst().setMyTurn(true);
        } else {
            this.playerList.get(index + 1).setMyTurn(true);
        }
    }

    // sets game's last round
    public void setLastRound(Player player) {
        this.lastRound = player.getTurnPlayed() + 1;
    }

    public int getLastRound() {
        return this.lastRound;
    }

    // add the selected player to the game
    public void addPlayerToGame(Player player) throws PlayerNotAddedException {
        if (currNumPlayer < numPlayer) {
            this.playerList.add(player);
            player.setGame(this);
            currNumPlayer++;
            this.table.getPlayerBoardMap().put(player, new Board(player));
            if (!this.playerList.contains(player)) {
                System.out.println("lancio eccezione");
                throw new PlayerNotAddedException(player);
            }
        } else {
            System.out.println("Error; max number of player reached: " + currNumPlayer);
        }
    }

    public void giveStartCard() throws CardNotAddedToHandException {
        for (Player player : this.playerList) {
            player.addToHand(deck.getStartDeck().poll());
        }
    }

    // gives two objective cards to each player, after that, each player will have to choose one of this
    public void givePrivateObjectiveCards() {
        for (Player player : playerList) {
            player.getObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
            player.getObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
        }
    }

    public void checkNickname(String nickname, Player player) throws NicknameAlreadyTakenException {
        for (Player playerToCheck : playerList) {
            if (playerToCheck.getNickname().equals(nickname) && !playerToCheck.equals(player)) {
                throw new NicknameAlreadyTakenException(playerList, null, null);
            }
        }
    }
}