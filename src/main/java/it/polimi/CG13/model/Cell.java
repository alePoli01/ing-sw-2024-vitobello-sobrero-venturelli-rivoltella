package it.polimi.CG13.model;

public class Cell {
    private PlayableCard cardPointer;   //pointer to the card object
    public final int weight;
    private boolean isFlipped;//to be intended as 'z coordinate' or height of the pointed card

    public Cell(PlayableCard cardPointer, int weight, boolean isFlipped) {
        this.cardPointer = cardPointer;
        this.weight = weight;
        this.isFlipped = isFlipped;
    }

    public boolean getIsFlipped() {
        return isFlipped;
    }

    public void setCardPointer(PlayableCard cardPointer) {
        this.cardPointer = cardPointer;
    }

    public PlayableCard getCardPointer() {
        return cardPointer;
    }
}