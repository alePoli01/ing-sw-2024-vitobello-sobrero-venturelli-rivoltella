package it.polimi.GC13.exception;

import it.polimi.GC13.model.PlayableCard;

    public class CardStillOnHandException extends Exception {
        public CardStillOnHandException(PlayableCard card) {
            super("Error model" + card.serialNumber +  "not placed.");
        }
}
