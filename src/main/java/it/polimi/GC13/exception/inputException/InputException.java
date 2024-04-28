package it.polimi.GC13.exception.inputException;

import it.polimi.GC13.view.View;

import java.io.IOException;

public interface InputException {

    void methodToRecall(View TUI) throws IOException;

    String getMessage() throws IOException;
}
