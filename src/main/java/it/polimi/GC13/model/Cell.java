package it.polimi.GC13.model;

import java.io.Serializable;

/**
 * Class that represents a playable card and the turn when it has been placed
 */
public class Cell implements Serializable {
    private final PlayableCard card;   //pointer to the card object
    public final int weight;
    public final boolean isFlipped;//to be intended as 'z coordinate' or height of the pointed card

    /**
     * cell constructor
     * @param card playable card
     * @param weight turn when the card has been placed
     * @param isFlipped side of the card, if true the card is placed on back else on the front
     */
    public Cell(PlayableCard card, int weight, boolean isFlipped) {
        this.card = card;
        this.weight = weight;
        this.isFlipped = isFlipped;
    }

    public PlayableCard getCardPointer() {
        return this.card;
    }
}