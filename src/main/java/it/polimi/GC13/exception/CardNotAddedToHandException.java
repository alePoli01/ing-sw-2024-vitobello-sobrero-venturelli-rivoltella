package it.polimi.GC13.exception;

import it.polimi.GC13.model.PlayableCard;

public class CardNotAddedToHandException extends Exception {
    public CardNotAddedToHandException(PlayableCard drawnCard) {
        super(drawnCard.serialNumber + "not added to the hand");
    }
}
