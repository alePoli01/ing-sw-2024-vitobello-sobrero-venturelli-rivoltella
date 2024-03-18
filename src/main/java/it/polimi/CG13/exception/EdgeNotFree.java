package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class EdgeNotFree extends Exception {

    public EdgeNotFree(PlayableCard card, int edge) {
        super("Card" + card.getSerialNumber() + "doesn't have a valid linkable edge in" + edge + "position");
    }
}
