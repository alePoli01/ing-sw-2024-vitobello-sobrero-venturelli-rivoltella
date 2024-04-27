package it.polimi.GC13.exception;

import it.polimi.GC13.enums.TokenColor;

public class TokenAlreadyChosenException extends Exception {
    public TokenAlreadyChosenException(TokenColor token) {
        super(token.toString() + " is already chosen. Chose another one");
    }
}
