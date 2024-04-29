package it.polimi.GC13.exception;

import it.polimi.GC13.model.PlayableCard;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(PlayableCard card) {
        super("Error model " + card.serialNumber +  " not found.");
    }
}
