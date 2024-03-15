package it.polimi.CG13.model;

import it.polimi.CG13.enums.Color;

import java.util.ArrayList;

public class Player {
    private final String nickname;  //username of the palyer
    private Color token; //token chosen by the Player
    private ArrayList<PlayableCard> hand = new ArrayList<PlayableCard>(); //hand of the palyer ==> 3 cards max
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
    public void setMyTurn() {
        this.myTurn = true;
    }
    public int getTurnPlayed() {
        return turnPlayed;
    }
    public void setTurnPlayed(int turnPlayed) {
        this.turnPlayed = turnPlayed;
    }
}
