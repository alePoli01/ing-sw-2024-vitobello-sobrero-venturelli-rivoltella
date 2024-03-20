package it.polimi.CG13.exception;

import it.polimi.CG13.model.PlayableCard;

    public class CardStillOnHand extends Exception {
        public CardStillOnHand(PlayableCard card) {
            super("Error model" + card.getSerialNumber() +  "not placed.");
        }
}
