package it.polimi.GC13.model;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnNickNameAlreadyTakenMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnDealCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnDealPrivateObjectiveCardsMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

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
    private final String gameName;
    private final Observer observer;

    public Game(int numPlayer, String gameName) {
        this.gameName = gameName;
        this.gameState = GameState.JOINING;
        this.table = new Table();
        this.numPlayer = numPlayer;
        this.playerList = new ArrayList<>();
        this.deck = new Deck();
        this.currNumPlayer = 0;
        this.observer = new Observer();
    }

    public String getGameName() {
        return this.gameName;
    }

    public Observer getObserver() {
        return this.observer;
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
        int i = random.nextInt(playerList.size());
        int cont = 0;
        while (cont < playerList.size()) {
            if (i < playerList.size()) {
                playerList.get(i).setPosition(Position.values()[cont]);
                System.out.println(playerList.get(i).getNickname() + " " + playerList.get(i).getPosition());
                cont++;
                i++;
            } else {
                break;
            }
        }
        i = 0;
        while (cont < playerList.size()) {
            playerList.get(i).setPosition(Position.values()[cont]);
            System.out.println(playerList.get(i).getNickname() + " " + playerList.get(i).getPosition());
            cont++;
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
    public void addPlayerToGame(Player player) throws GenericException {
        if (currNumPlayer < numPlayer) {
            this.playerList.add(player);
            player.setGame(this);
            currNumPlayer++;
            this.table.getPlayerBoardMap().put(player, new Board(player));
            if (!this.playerList.contains(player)) {
                System.out.println("lancio eccezione");
                this.observer.notifyClients(new OnPlayerNotAddedMessage(player.getNickname(), this.gameName));
                throw new GenericException(player.getNickname() + " was not added to the game " + this.gameName);
            }
            this.observer.notifyClients(new OnPlayerAddedToGameMessage(this.getCurrNumPlayer(), this.numPlayer));
        } else {
            System.out.println("Error; max number of player reached: " + currNumPlayer);
        }
    }

    public void dealStartCard() throws CardNotAddedToHandException {
        for (Player player : this.playerList) {
            player.addToHand(deck.getStartDeck().poll());
            // send message to listener
            this.observer.notifyClients(new OnDealCardMessage(player.getNickname(), player.getHandCardSerialNumber()));
        }
    }

    // gives two objective cards to each player, after that, each player will have to choose one of this
    public void dealPrivateObjectiveCards() {
        for (Player player : playerList) {
            player.getObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
            player.getObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
            this.observer.notifyClients(new OnDealPrivateObjectiveCardsMessage(player.getNickname(), player.getPrivateObjectiveCardSerialNumber()));
        }
    }

    public void checkNickname(String nickname, Player player) throws GenericException {
        for (Player playerToCheck : playerList) {
            if (playerToCheck.getNickname().equals(nickname) && !playerToCheck.equals(player)) {
                this.observer.notifyClients(new OnNickNameAlreadyTakenMessage(nickname));
                throw new GenericException("Nickname: " + nickname + " was already choose");
            }
        }
    }
}