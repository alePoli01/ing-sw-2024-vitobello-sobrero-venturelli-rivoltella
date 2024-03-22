package it.polimi.CG13.exception;

public class NoOtherCards extends Throwable {
    public NoOtherCards(String deckType) {
        super(deckType + "deck is empty");
    }
}
