package it.polimi.GC13.model;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.messages.fromserver.*;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnPlayerNotAddedMessage;

import java.io.Serializable;
import java.util.*;
import java.util.stream.Collectors;

public class Game implements Serializable {
    private GameState gameState;
    private final Table table;
    public final int numPlayer;
    private int currNumPlayer;
    private final List<Player> playerList;
    private int lastRound;
    private final String gameName;
    private final Observer observer;
    private final Map<String, List<String>> chat = new HashMap<>();
    private Player winner;

    public Game(int numPlayer, String gameName) {
        this.gameName = gameName;
        this.gameState = GameState.JOINING;
        this.table = new Table(this);
        this.numPlayer = numPlayer;
        this.playerList = new ArrayList<>();
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

    public Table getTable() {
        return table;
    }

    public int getCurrNumPlayer() {
        return currNumPlayer;
    }

    public List<Player> getPlayerList() {
        return this.playerList;
    }

    // give firsts 3 cards to each player
    public void giveFirstCards() throws GenericException {
        List<PlayableCard> firstHand = new ArrayList<>();
        for (Player player : this.playerList) {
            for (int i = 0; i < 2; i++) {
                firstHand.add(this.table.getDeck().getResourceDeck().removeFirst());
            }
            firstHand.add(this.table.getDeck().getGoldDeck().removeFirst());
            player.addToHand(firstHand);
            firstHand.clear();
        }
    }

    // add common objective card to the table
    public void setCommonObjectiveCards() {
        LinkedList<Integer> commonSerialObjectiveCards = new LinkedList<>();
        this.getTable().getCommonObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
        this.getTable().getCommonObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
        this.getTable().getCommonObjectiveCard().forEach(card -> commonSerialObjectiveCards.add(card.serialNumber));
        this.observer.notifyClients(new OnDealCommonObjectiveCardMessage(commonSerialObjectiveCards));
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
                        }
                        this.playerList.get(r).setPosition(p);
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
        this.observer.notifyClients(new OnSetLastTurnMessage(player.getNickname(), player.getPosition()));
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
            player.addToHand(List.of(this.table.getDeck().getStartDeck().removeFirst()));
        }
    }

    // gives two objective cards to each player, after that, each player will have to choose one of this
    public void dealPrivateObjectiveCards() {
        for (Player player : playerList) {
            player.getPrivateObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
            player.getPrivateObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
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

    public void registerChatMessage(String sender, String recipient, String message) {
        if (!recipient.equalsIgnoreCase("global")) {
            // grants always the same key for chat between two players
            String key = sender.compareTo(recipient) < 0 ? sender.concat(recipient) : recipient.concat(sender);

            if (this.chat.containsKey(key)) {
                synchronized (this.chat.get(key)) {
                    this.chat.get(key).add(message);
                }
            } else {
                synchronized (this.chat) {
                    this.chat.put(key, new LinkedList<>(Collections.singletonList(message)));
                }
            }
        } else {
            if (this.chat.containsKey("global")) {
                synchronized (this.chat.get("global")) {
                    this.chat.get("global").add(message);
                }
            } else {
                synchronized (this.chat) {
                    this.chat.put("global", new LinkedList<>(Collections.singletonList(message)));
                }
            }
        }

        this.observer.notifyClients(new OnNewMessage(sender, recipient, message));
    }

    public String setWinner() {
        this.finalScoreCalculation();
        this.winner = this.playerList.getFirst();
        for (Player player : this.playerList) {
            if (player.getScore() > winner.getScore()) {
                winner = player;
            }
        }
        this.observer.notifyClients(new OnGameWinnerMessage(this.winner.getNickname()));
        return winner.getNickname();
    }

    /**
     * method used to calculate final score with private and common objective cards
     */
    private void finalScoreCalculation() {
        for (Player player : this.playerList) {
            //set player score = player score + player's objective points(based on his board)
            this.table.setPlayerScore(player,
                    player.getTable().getPlayersScore().get(player)
                            + player.getPrivateObjectiveCard().getFirst().getObjectivePoints(player.getBoard())
            );
        }
    }
}