package it.polimi.CG13.model;

import it.polimi.CG13.enums.Color;
import it.polimi.CG13.exception.*;

import java.util.ArrayList;

public class Player {
    private final String nickname;  //username of the palyer
    private Color token; //token chosen by the Player
    private ArrayList<PlayableCard> hand; //hand of the palyer ==> 3 cards max
    private ObjectiveCard objectiveCard; //Hidden goal of the player
    private boolean myTurn; //true if it's the player turn
    private int turnPlayed; //number of the current turn
    private int position;  //position of the player (1-4)

    //at the creation each player has only its nickname, everything else is defined in the setup phase
    public Player(String nickname) {
        this.nickname = nickname;
        this.token = null;
        this.turnPlayed = 0;
        this.myTurn = false;
        this.hand = new ArrayList<PlayableCard>();
    }

    public String getNickname() {
        return nickname;
    }

    public Color getToken() {
        return token;
    }

    public void setToken(Color token) {
        this.token = token;
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
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

    public ObjectiveCard getObjectiveCard() {
        return objectiveCard;
    }

    public void setObjectiveCard(ObjectiveCard objectiveCard) {
        this.objectiveCard = objectiveCard;
    }
}
