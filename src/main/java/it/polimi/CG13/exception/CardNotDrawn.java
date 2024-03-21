package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotDrawn extends Exception {
    public CardNotDrawn(PlayableCard card) {
        super("Error model" + card.getSerialNumber() +  "not drawn.");
    }
}
