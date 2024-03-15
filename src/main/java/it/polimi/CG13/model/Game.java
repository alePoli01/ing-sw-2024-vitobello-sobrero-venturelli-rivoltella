package it.polimi.CG13.model;

import java.util.List;

public class Game  {

    /*TODO: controllare chi usa GameState(controll o model?)
            capire se ci deve essere un attributo che rappresenta in quale GameState sono
    */
    private enum GameState{START,MID,END}

    private Deck deck;
    private ObjectiveCard[] commonObjective;
    private int numPlayer;
    private Player[] playerList;
    private int round;

    public void randomizeArray(){

    }
    public void addPlayer(){

    }
    public void setPlayerList(Player[] playerList) {
        this.playerList = playerList;
    }
    public Player[] getPlayerList() {
        return playerList;
    }

    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }
    public int getNumPlayer() {
        return numPlayer;
    }
}
