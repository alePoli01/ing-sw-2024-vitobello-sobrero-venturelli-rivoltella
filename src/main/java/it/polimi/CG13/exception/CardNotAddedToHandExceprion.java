package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotAddedToHandExceprion extends Throwable {
    public CardNotAddedToHandExceprion(PlayableCard drawnCard) {
        super(drawnCard.getSerialNumber() + "not added to the hand");
    }
}
