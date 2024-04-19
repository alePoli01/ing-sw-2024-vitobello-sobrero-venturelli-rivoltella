package it.polimi.GC13.exception;

public class NicknameAlreadyTakenException extends Exception {
    public NicknameAlreadyTakenException() {
        super("Nickname already taken, choose another nickname");
    }
}
