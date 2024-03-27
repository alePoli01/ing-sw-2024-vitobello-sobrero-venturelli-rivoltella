package it.polimi.GC13.model;

public class Cell {
    private PlayableCard cardPointer;   //pointer to the card object
    public final int weight;
    public final boolean isFlipped;//to be intended as 'z coordinate' or height of the pointed card

    public Cell(PlayableCard cardPointer, int weight, boolean isFlipped) {
        this.cardPointer = cardPointer;
        this.weight = weight;
        this.isFlipped = isFlipped;
    }

    public void setCardPointer(PlayableCard cardPointer) {
        this.cardPointer = cardPointer;
    }

    public PlayableCard getCardPointer() {
        return cardPointer;
    }
}