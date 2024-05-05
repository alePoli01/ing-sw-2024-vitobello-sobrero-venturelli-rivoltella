package it.polimi.GC13.model;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.*;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

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
        this.table = new Table(this);
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
    public void giveFirstCards() throws GenericException {
        for (Player player : this.playerList) {
            int[] availableCards = new int[3];
            for (int i = 0; i < 3; i++) {
                player.addToHand(deck.getResourceDeck().removeFirst());
                availableCards[i] = player.getHand().getLast().serialNumber;
            }
            this.observer.notifyClients(new OnDealCardMessage(player.getNickname(), availableCards));
        }
    }

    // add common objective card to the table
    public void setCommonObjectiveCards() {
        int[] commonObjectiveCards = new int[2];
        commonObjectiveCards[0] = this.getTable().setCommonObjectiveCard(0, getDeck().getObjectiveDeck().removeFirst());
        commonObjectiveCards[1] = this.getTable().setCommonObjectiveCard(1, getDeck().getObjectiveDeck().removeFirst());
        this.observer.notifyClients(new OnDealCommonObjectiveCardMessage(commonObjectiveCards));
    }

    // set players position
    public void setPlayersPosition() {
        Random random = new Random();

        Arrays.stream(Position.values())
                .limit(playerList.size())
                .forEach(p ->{
                    int r = random.nextInt(playerList.size());

                    if (this.playerList.get(r).getPosition() == null) {
                        this.playerList.get(r).setPosition(p);
                    } else {
                        while (this.playerList.get(r).getPosition() != null) {
                            r = random.nextInt(playerList.size());
                            if (this.playerList.get(r).getPosition() == null) {
                                this.playerList.get(r).setPosition(p);
                            }
                        }
                    }
                });

        Map<String, Position> playerPositions = playerList.stream()
                .collect(Collectors.toMap(Player::getNickname, Player::getPosition));

        this.observer.notifyClients(new OnTurnSetMessage(playerPositions));
    }

    // updates players turn at the end of every round
    public void setPlayerTurn(Player player) {
        Position nextPlayerPosition = player.getPosition().next(playerList.size());
        this.playerList.stream()
                .filter(p -> p.getPosition().equals(nextPlayerPosition))
                .forEach(p -> p.setMyTurn(true));
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
            System.out.println(player.getNickname() + " notified");
            System.out.println(this.observer.listenerList.size() + " listeners");
            this.observer.notifyClients(new OnPlayerAddedToGameMessage(this.getCurrNumPlayer(), this.numPlayer));
        } else {
            System.out.println("Error; max number of player reached: " + currNumPlayer);
        }
    }

    public void dealStartCard() throws GenericException {
        for (Player player : this.playerList) {
            player.addToHand(deck.getStartDeck().removeFirst());
            // send message to listener
            this.observer.notifyClients(new OnDealCardMessage(player.getNickname(), player.getHandCardSerialNumber()));
        }
    }

    // gives two objective cards to each player, after that, each player will have to choose one of this
    public void dealPrivateObjectiveCards() {
        for (Player player : playerList) {
            player.getPrivateObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
            player.getPrivateObjectiveCard().add(this.getDeck().getObjectiveDeck().removeFirst());
            this.observer.notifyClients(new OnDealPrivateObjectiveCardsMessage(player.getNickname(), player.getPrivateObjectiveCardSerialNumber()));
        }
    }

    public void checkNickname(String nickname, Player player) throws GenericException {
        for (Player playerToCheck : playerList) {
            if (playerToCheck.getNickname().equals(nickname) && !playerToCheck.equals(player)) {
                throw new GenericException("Nickname: " + nickname + " was already choose");
            }
        }
    }
}