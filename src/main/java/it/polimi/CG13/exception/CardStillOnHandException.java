package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

    public class CardStillOnHandException extends Exception {
        public CardStillOnHandException(PlayableCard card) {
            super("Error model" + card.serialNumber +  "not placed.");
        }
}
