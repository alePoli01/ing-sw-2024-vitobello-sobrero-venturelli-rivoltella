package it.polimi.CG13.model;

import it.polimi.CG13.enums.GameState;

import java.util.List;

public class Game  {

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
        this.gameState = gameState;
        this.deck = deck;
        this.table = table;
        this.numPlayer = numPlayer;
        this.playerList = playerList;
        this.round = round;
    }

    public void randomizeArray(){
    }
    public void setNumPlayer(int numPlayer) {
        this.numPlayer = numPlayer;
    }
    public int getNumPlayer() {
        return numPlayer;
    }
}
