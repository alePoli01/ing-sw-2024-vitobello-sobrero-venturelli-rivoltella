package it.polimi.GC13.exception;

import it.polimi.GC13.model.PlayableCard;

public class WrongCardException extends Exception{
    public WrongCardException(PlayableCard card, int error) {
        super(getMessage(card, error));
    }

    private static String getMessage(PlayableCard card, int valore) {
        return switch (valore) {
            case 0 -> "Error: the card " + card.serialNumber + "is not acceptable";
            case 1 -> "Error: the card is not found";
            case 2 -> "Error: file not found";
            case 3 -> "Error: directory not found";
            default -> "Everything's ok!"; //non serve, ma se lo tolgo mi dà errore
        };
    }



}
