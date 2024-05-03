package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;

import java.util.ArrayList;

public class OnTokenAlreadyChosenMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final ArrayList<TokenColor> availableTokenColors;
    private final String playerNickname;

    public OnTokenAlreadyChosenMessage(String playerNickname, TokenColor token, ArrayList<TokenColor> tokenColors) {
        errorMessage = token.toString() + " is already chosen. Available colors are:";
        this.availableTokenColors = tokenColors;
        this.playerNickname = playerNickname;
    }

    @Override
    public void pokeMessageDispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(null);
    }

    @Override
    public void notifyClient(ClientInterface client) {
        client.sendMessage(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }

    @Override
    public void methodToRecall(View TUI) {
        TUI.tokenSetupPhase(0, availableTokenColors,0);
    }

    @Override
    public String getErrorMessage() {
        return this.errorMessage;
    }

    @Override
    public String getPlayerNickname() {
        return this.playerNickname;
    }
}
