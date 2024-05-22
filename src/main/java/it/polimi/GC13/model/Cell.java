package it.polimi.GC13.model;

import java.io.Serializable;

public class Cell implements Serializable {
    private final PlayableCard card;   //pointer to the card object
    public final int weight;
    public final boolean isFlipped;//to be intended as 'z coordinate' or height of the pointed card

    public Cell(PlayableCard card, int weight, boolean isFlipped) {
        this.card = card;
        this.weight = weight;
        this.isFlipped = isFlipped;
    }

    public PlayableCard getCardPointer() {
        return card;
    }
}