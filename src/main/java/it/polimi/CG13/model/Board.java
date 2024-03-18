package it.polimi.CG13.model;

import java.util.Arrays;
import java.util.HashMap;

public class Board {

    private HashMap <Coordinates,Cell> boardMap;
    private Player owner;               //owner of the board
    private int score;
    private int[] objectsCollected;     //counter for each type of object present on the board
    private int[] reignsCollected;      //counter for each type of reigns present on the board

    //initialize all the values to zero
    public Board() {
        this.owner = null;
        this.score=0;
        objectsCollected=new int[3];
        reignsCollected=new int[4];
        Arrays.fill(objectsCollected, 0);
        Arrays.fill(objectsCollected, 0);
    }

    public int getScore() {
        return score;
    }
    public void setScore(int score) {
        this.score=score;
    }

    public Player getOwner() {
        return owner;
    }
    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public int getObjectsCollected(int position){
        return objectsCollected[position];
    }
    public void setObjectsCollected(int position,int value){
        this.objectsCollected[position]=value;
    }

    public int getReignsCollected(int position){
        return reignsCollected[position];
    }
    public void setReignsCollected(int position,int value){
        this.reignsCollected[position]=value;
    }

    public void isPossibleToPlace() {
        // lavorare su mappa
    }
}
