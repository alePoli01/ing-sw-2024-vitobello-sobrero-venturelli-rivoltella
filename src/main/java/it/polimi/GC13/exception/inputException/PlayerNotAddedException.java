package it.polimi.GC13.exception.inputException;

import it.polimi.GC13.model.Player;
import it.polimi.GC13.view.View;

public class PlayerNotAddedException extends Exception implements InputException {
    public PlayerNotAddedException(Player player) {
        super(player.getNickname() + " hasn't added to the game.");
    }

    @Override
    public void methodToRecall(View TUI) {
        TUI.checkForExistingGame();
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
