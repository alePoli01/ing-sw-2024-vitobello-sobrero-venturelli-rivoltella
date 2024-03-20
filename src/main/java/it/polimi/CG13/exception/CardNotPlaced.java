package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotPlaced extends Exception {
    public CardNotPlaced(PlayableCard card) {
        super("Error model" + card.getSerialNumber() +  "not placed.");
    }
}
