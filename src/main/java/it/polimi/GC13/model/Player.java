package it.polimi.GC13.model;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.*;
import java.util.ArrayList;
import java.util.LinkedList;

public class Player {
    private final String nickname;  //username of the player
    private TokenColor token; //token chosen by the Player
    private final ArrayList<PlayableCard> hand; //hand of the player ==> 3 cards max
    private LinkedList<ObjectiveCard> objectiveCard; // need an array to present 2 objective cards to the player
    private boolean myTurn; //true if it's the player turn
    private int turnPlayed; //number of the current turn
    private Position position;  //position of the player (1-4)
    private Game game;


    //at the creation each player has only its nickname, everything else is defined in the setup phase
    public Player(String nickname) {
        this.nickname = nickname;
        this.turnPlayed = 0;
        this.myTurn = false;
        this.hand = new ArrayList<PlayableCard>();
    }

    public String getNickname() {
        return nickname;
    }

    public TokenColor getToken() {
        return token;
    }

    public void setToken(TokenColor token) {
        this.token = token;
    }

    public Position getPosition() {
        return position;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LinkedList<ObjectiveCard> getObjectiveCard() {
        return objectiveCard;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
    }

    public Game getGame() {
        return game;
    }

    public Board getBoard() {
        return this.game.getTable().getPlayerBoardMap().get(this);
    }

    public Table getTable() {
        return this.game.getTable();
    }

    public ArrayList<PlayableCard> getHand() {
        return this.hand;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
    }

    // check it is player's turn before playing
    public void checkMyTurn() throws NotMyTurnException {
        if (!myTurn) {
            throw new NotMyTurnException(this.getNickname());
        }
    }

    // remove placedCard after it is placed on the board
    public void handUpdate(PlayableCard placedCard) throws CardStillOnHandException {
        hand.remove(placedCard);
        if (hand.contains(placedCard)) {
            throw new CardStillOnHandException(placedCard);
        }
    }

    // add drawnCard to the hand
    public void addToHand(PlayableCard drawnCard) throws CardNotAddedToHandException {
        hand.add(drawnCard);
        if (!hand.contains(drawnCard)) {
            throw new CardNotAddedToHandException(drawnCard);
        }
    }


}
