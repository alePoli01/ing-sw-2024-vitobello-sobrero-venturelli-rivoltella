package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotAddedToHandException extends Exception {
    public CardNotAddedToHandException(PlayableCard drawnCard) {
        super(drawnCard.getSerialNumber() + "not added to the hand");
    }
}
