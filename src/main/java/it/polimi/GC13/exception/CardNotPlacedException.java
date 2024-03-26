package it.polimi.GC13.exception;

import it.polimi.GC13.model.PlayableCard;

public class CardNotPlacedException extends Exception {
    public CardNotPlacedException(PlayableCard card) {
        super("Error model" + card.serialNumber +  "not placed.");
    }
}
