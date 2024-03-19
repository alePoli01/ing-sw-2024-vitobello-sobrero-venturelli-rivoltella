package it.polimi.CG13.model;

public class Cell {
    private PlayableCard cardPointer;   //pointer to the card object
    final int weight;                 //to be intended as 'z coordinate' or height of the pointed card


    public Cell(PlayableCard cardPointer, int weight) {
        this.cardPointer = cardPointer;
        this.weight = weight;
    }

    public int getWeight() {
        return weight;
    }

    public void setCardPointer(PlayableCard cardPointer) {
        this.cardPointer = cardPointer;
    }
    public PlayableCard getCardPointer() {
        return cardPointer;
    }
}