package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotFoundException extends Exception {
    public CardNotFoundException(PlayableCard card) {
        super("Error model" + card.serialNumber +  "not drawn.");
    }
}
