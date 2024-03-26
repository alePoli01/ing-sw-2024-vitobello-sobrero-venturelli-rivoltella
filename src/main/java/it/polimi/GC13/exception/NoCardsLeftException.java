package it.polimi.GC13.exception;

public class NoCardsLeftException extends Throwable {
    public NoCardsLeftException(String deckType) {
        super(deckType + "deck is empty");
    }
}
