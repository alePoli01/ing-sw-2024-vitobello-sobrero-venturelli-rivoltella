package it.polimi.GC13.model;

import java.io.Serializable;


/**
 * {@code Cell} class represents a cell on the board containing a playable card.
 */
public class Cell implements Serializable {

    /**
     * The playable card stored in this cell.
     */
    private final PlayableCard card;   //pointer to the card object

    /**
     * The weight or 'z coordinate' of the card.
     */
    public final int weight;

    /**
     * Indicates whether the card is flipped (back side) or not.
     */
    public final boolean isFlipped;



    /**
     * Constructs a cell with the specified card, weight, and flipped state.
     *
     * @param card     The playable card stored in this cell.
     * @param weight   The weight or 'z coordinate' of the card.
     * @param isFlipped side of the card: if true the card is placed on back else on the front.
     */
    public Cell(PlayableCard card, int weight, boolean isFlipped) {
        this.card = card;
        this.weight = weight;
        this.isFlipped = isFlipped;
    }

    /**
     * Retrieves the playable card stored in this cell.
     *
     * @return The playable card stored in this cell.
     */
    public PlayableCard getCardPointer() {
        return this.card;
    }
}