package it.polimi.GC13.exception.inputException;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.view.View;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TokenAlreadyChosenException extends Exception implements InputException {
    private final List<TokenColor> availableTokenColors;

    public TokenAlreadyChosenException(TokenColor token, ArrayList<TokenColor> tokenColors) {
        super(token.toString() + " is already chosen. Available colors are:");
        this.availableTokenColors = tokenColors;
    }

    @Override
    public void methodToRecall(View TUI) throws IOException {
        TUI.tokenSetupPhase(0, availableTokenColors);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
