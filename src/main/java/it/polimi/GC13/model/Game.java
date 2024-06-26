package it.polimi.GC13.model;

import it.polimi.GC13.app.DiskManager;
import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.messages.fromserver.*;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnPlayerNotAddedMessage;
import it.polimi.GC13.view.GUI.game.ChatMessage;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;


/**
 * The {@code Game} class represents the core model for a game instance;
 * it's created for each game and has reference to each part of the model.
 <br>
 * It manages the state of the game, players, and interactions within the game.
 */
public class Game implements Serializable {
    private GameState gameState;
    private final Table table;
    public final int numPlayer;
    private int currNumPlayer = 0;
    private final List<Player> playerList = new ArrayList<>();
    private int lastRound;
    private final String gameName;
    private transient Observer observer;
    private final Map<String, List<ChatMessage>> chat = new HashMap<>();

    /**
     * Constructs a new {@code Game} instance.
     *
     * @param numPlayer the number of players who will play the game, set by the creator.
     * @param gameName the name of the game, which serves as a unique readable identifier.
     */
    public Game(int numPlayer, String gameName) {
        this.gameName = gameName;
        this.gameState = GameState.JOINING;
        this.table = new Table(this);
        this.numPlayer = numPlayer;
        this.setObserver();
    }

    /**
     * Returns the name of the game.
     *
     * @return the game name.
     */
    public String getGameName() {
        return this.gameName;
    }

    /**
     * Returns the observer associated with this game.
     *
     * @return the observer.
     */
    public Observer getObserver() {
        return this.observer;
    }

    /**
     * Sets the observer for the game.
     */
    public void setObserver() {
        this.observer = new Observer(new DiskManager(), this);
    }

    /**
     * Sets the current state of the game.
     *
     * @param newState the new game state.
     */
    public void setGameState(GameState newState) {
        this.gameState = newState;
    }

    /**
     * Returns the current state of the game.
     *
     * @return the game state.
     */
    public GameState getGameState() {
        return this.gameState;
    }

    /**
     * Returns the game table.
     *
     * @return the table.
     */
    public Table getTable() {
        return table;
    }

    /**
     * Returns the current number of players in the game.
     *
     * @return the current number of players.
     */
    public int getCurrNumPlayer() {
        return currNumPlayer;
    }

    /**
     * Returns the list of players in the game.
     *
     * @return the player list.
     */
    public List<Player> getPlayerList() {
        return this.playerList;
    }

    /**
     * Deals the initial set of cards to each player.
     *
     * @throws GenericException if an error occurs while dealing cards.
     */
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

    /**
     * Adds common objective cards to the table.
     */
    // add common objective card to the table
    public void setCommonObjectiveCards() {
        LinkedList<Integer> commonSerialObjectiveCards = new LinkedList<>();
        this.getTable().getCommonObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
        this.getTable().getCommonObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
        this.getTable().getCommonObjectiveCard().forEach(card -> commonSerialObjectiveCards.add(card.serialNumber));
        this.observer.notifyClients(new OnDealCommonObjectiveCardMessage(commonSerialObjectiveCards));
    }

    /**
     * Sets the positions of players in the game.
     */
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

    /**
     * Updates the turn for the next player at the end of every round.
     *
     * @param player the current player.
     */
    // updates players turn at the end of every round
    public void setPlayerTurn(Player player) {
        Position nextPlayerPosition = player.getPosition().next(playerList.size());
        this.playerList.stream()
                .filter(p -> p.getPosition().equals(nextPlayerPosition))
                .forEach(p -> p.setMyTurn(true));
    }

    /**
     * Sets the last round for the game.
     *
     * @param player the player who triggers the last round.
     */
    // sets game's last round
    public void setLastRound(Player player) {
        if (this.lastRound == 0) {
            this.lastRound = player.getTurnPlayed() + 1;
            this.observer.notifyClients(new OnSetLastTurnMessage(player.getNickname()));
        }
    }

    /**
     * Returns the last round of the game.
     *
     * @return the last round.
     */
    public int getLastRound() {
        return this.lastRound;
    }

    /**
     * Adds a player to the game.
     *
     * @param player the player to be added.
     * @throws GenericException if an error occurs while adding the player.
     */
    // add the selected player to the game
    public void addPlayerToGame(Player player) throws GenericException {
        if (currNumPlayer < numPlayer) {
            this.playerList.add(player);
            player.setGame(this);
            currNumPlayer++;
            this.table.getPlayerBoardMap().put(player, new Board(player));
            if (!this.playerList.contains(player)) {
                this.observer.notifyClients(new OnPlayerNotAddedMessage(player.getNickname(), this.gameName));
                throw new GenericException(player.getNickname() + " was not added to the game " + this.gameName);
            }
            System.out.println(player.getNickname() + " notified");
            System.out.println(this.observer.getListenerSize() + " listeners");
            this.observer.notifyClients(new OnPlayerAddedToGameMessage(this.getCurrNumPlayer(), this.numPlayer, this.getGameName()));
        } else {
            System.out.println("Error; max number of player reached: " + currNumPlayer);
        }
    }

    /**
     * Deals the starting card to each player.
     *
     * @throws GenericException if an error occurs while dealing the cards.
     */
    public void dealStartCard() throws GenericException {
        for (Player player : this.playerList) {
            player.addToHand(List.of(this.table.getDeck().getStartDeck().removeFirst()));
        }
    }

    /**
     * Deals private objective cards to each player.
     */
    // gives two objective cards to each player, after that, each player will have to choose one of this
    public void dealPrivateObjectiveCards() {
        for (Player player : playerList) {
            player.getPrivateObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
            player.getPrivateObjectiveCard().add(this.table.getDeck().getObjectiveDeck().removeFirst());
            this.observer.notifyClients(new OnDealPrivateObjectiveCardsMessage(player.getNickname(), player.getPrivateObjectiveCardSerialNumber()));
        }
    }

    /**
     * Checks if a nickname is already used by another player.
     *
     * @param nickname the nickname to check.
     * @param player the player who wants to use the nickname.
     * @throws GenericException if the nickname is already used.
     */
    public void checkNickname(String nickname, Player player) throws GenericException {
        for (Player playerToCheck : playerList) {
            if (playerToCheck.getNickname().equals(nickname) && !playerToCheck.equals(player)) {
                throw new GenericException("Nickname: " + nickname + " was already choose");
            }
        }
    }

    /**
     * Registers a chat message.
     *
     * @param sender the sender of the message.
     * @param recipient the recipient of the message.
     * @param message the message content.
     */
    public void registerChatMessage(String sender, String recipient, String message) {
        String key;
        if (!recipient.equals("global")) {
            // grants always the same key for chat between two players
            key = sender.compareTo(recipient) < 0 ? sender.concat(recipient) : recipient.concat(sender);
        } else {
            // key is global
            key = recipient;
        }

        if (this.chat.containsKey(key)) {
            synchronized (this.chat.get(key)) {
                this.chat.get(key).add(new ChatMessage(sender, message));
            }
        } else {
            synchronized (this.chat) {
                this.chat.put(key, new LinkedList<>(Collections.singletonList(new ChatMessage(sender, message))));
            }
        }

        this.observer.notifyClients(new OnNewMessage(sender, recipient, message));
    }

    /**
     * Determines and sets the winner(s) of the game.
     *
     * @return a set of the winners' nicknames.
     */
    public Set<String> setWinner() {
        System.out.println("Calculating winner for game " + this.gameName);
        Map<Player, Integer> objectivesAchieved = new HashMap<>();
        // map used to track for each player which are the objective cards that gave him points and the amount
        Map<String, List<ObjectiveAchieved>> objectiveAchievedMap = new ConcurrentHashMap<>();

        // in case two or more players have the same final score, the number of objectives achieved will be used to determine the winner
        this.playerList.forEach(player -> objectivesAchieved.put(player, this.finalScoreCalculation(player, objectiveAchievedMap)));

        // Filter for players with the maximum score
        Set<Player> playersWithHighestScore = this.getTable().getPlayersScore().entrySet().stream()
                .max(Map.Entry.comparingByValue())
                .map(Map.Entry::getKey)
                .stream()
                .collect(Collectors.toSet());
        // if the size is 1, it's not necessary to check how many objective each player has achieved and the control is skipped
        Set<Player> winner;
        if (playersWithHighestScore.size() > 1) {
            Map<Player, Integer> objectiveAchievedByPlayersWithHighestScore = new HashMap<>();
            playersWithHighestScore.forEach(player -> objectiveAchievedByPlayersWithHighestScore.put(player, objectivesAchieved.get(player)));

            // adds to the winner set players with the highest number of objectives achieved
            winner = objectiveAchievedByPlayersWithHighestScore.entrySet().stream()
                    .max(Map.Entry.comparingByValue())
                    .map(Map.Entry::getKey)
                    .stream()
                    .collect(Collectors.toSet());
        }else{
            winner =playersWithHighestScore;
        }

        // create the set of the winners' nickname to send to the clients (views)
        Set<String> winnersNickname = winner.stream().map(Player::getNickname).collect(Collectors.toSet());
        this.observer.notifyClients(new OnGameWinnerMessage(winnersNickname, objectiveAchievedMap));
        return winnersNickname;
    }

    /**
     * Calculates the final score for a player based on private and common objective cards.
     *
     * @param player the player whose score is being calculated.
     * @param objectiveAchievedMap a map to track objectives achieved by each player.
     * @return the number of objectives achieved by the player.
     */
    private int finalScoreCalculation(Player player, Map<String, List<ObjectiveAchieved>> objectiveAchievedMap) {
        objectiveAchievedMap.put(player.getNickname(), new LinkedList<>());
        // used to track how many objective the player has achieved
        AtomicInteger numberObjectivesAchieved = new AtomicInteger(0);

        // points scored with objective cards
        AtomicInteger commonObjectiveCardsPointsGiven = new AtomicInteger(0);
        table.getCommonObjectiveCard().forEach(objectiveCard -> {
                    // if the player achieved a point with a common objective card, the counter is incremented
                    if (objectiveCard.getObjectivePoints(player.getBoard()) > 0) {
                        numberObjectivesAchieved.incrementAndGet();
                        int pointsGiven = objectiveCard.getObjectivePoints(player.getBoard());
                        commonObjectiveCardsPointsGiven.addAndGet(pointsGiven);
                        objectiveAchievedMap.get(player.getNickname()).add(new ObjectiveAchieved(objectiveCard.serialNumber, pointsGiven));
                    }
                });

        int pointsGiven = player.getPrivateObjectiveCard().getFirst().getObjectivePoints(player.getBoard());
        // if the player achieved a point with his private card, the counter is incremented
        if (pointsGiven > 0) {
            numberObjectivesAchieved.incrementAndGet();
            objectiveAchievedMap.get(player.getNickname()).add(new ObjectiveAchieved(player.getPrivateObjectiveCard().getFirst().serialNumber, pointsGiven));
        }

        // add player's final score = player's private objective points (based on his board) + common objective points (based on his board)
        this.table.addPlayerScore(player, pointsGiven + commonObjectiveCardsPointsGiven.get());
        return numberObjectivesAchieved.get();
    }

    /**
     * Closes the game and handles the disconnection of a client.
     *
     * @param disconnectedClient the client interface of the disconnected client.
     * @param disconnectedPlayerName the name of the disconnected player.
     */
    public void closeGame(ClientInterface disconnectedClient, String disconnectedPlayerName){
        this.observer.removeListener(disconnectedClient);
        this.observer.notifyClients(new OnPlayerDisconnected(disconnectedPlayerName));
    }
}