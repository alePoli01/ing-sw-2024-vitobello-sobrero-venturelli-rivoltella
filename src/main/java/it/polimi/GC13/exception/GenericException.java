package it.polimi.GC13.exception;

/**
 * Custom exception class for handling generic exceptions in the application.
 * Extends {@link Exception}.
 */
public class GenericException extends Exception {

    /**
     * Constructs a new {@code GenericException} with the specified error message.
     *
     * @param errorMessage the error message associated with this exception
     */
    public GenericException(String errorMessage) {
        super(errorMessage);
    }
}
