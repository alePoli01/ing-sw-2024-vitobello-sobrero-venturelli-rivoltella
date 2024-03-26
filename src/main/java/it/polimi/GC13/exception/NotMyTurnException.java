package it.polimi.GC13.exception;

public class NotMyTurnException extends Exception {
    public NotMyTurnException(String nickname) {
        super("It's not" + nickname + "turn.");
    }
}
