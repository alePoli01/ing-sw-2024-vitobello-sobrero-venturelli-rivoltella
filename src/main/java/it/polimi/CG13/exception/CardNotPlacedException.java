package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotPlacedException extends Exception {
    public CardNotPlacedException(PlayableCard card) {
        super("Error model" + card.serialNumber +  "not placed.");
    }
}
