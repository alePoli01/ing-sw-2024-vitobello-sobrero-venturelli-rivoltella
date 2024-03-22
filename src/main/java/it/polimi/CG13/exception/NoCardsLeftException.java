package it.polimi.CG13.exception;

public class NoCardsLeftException extends Throwable {
    public NoCardsLeftException(String deckType) {
        super(deckType + "deck is empty");
    }
}
