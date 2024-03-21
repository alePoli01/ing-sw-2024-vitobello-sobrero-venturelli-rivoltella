package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

public class CardNotAddedToHand extends Throwable {
    public CardNotAddedToHand(PlayableCard drawnCard) {
        super(drawnCard.getSerialNumber() + "not added to the hand");
    }
}
