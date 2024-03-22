package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotFound extends Exception {
    public CardNotFound(PlayableCard card) {
        super("Error model" + card.getSerialNumber() +  "not drawn.");
    }
}
