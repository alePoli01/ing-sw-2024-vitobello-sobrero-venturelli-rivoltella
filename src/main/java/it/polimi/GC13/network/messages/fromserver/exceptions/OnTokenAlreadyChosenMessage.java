package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;

import java.rmi.RemoteException;
import java.util.ArrayList;

public class OnTokenAlreadyChosenMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final ArrayList<TokenColor> availableTokenColors;
    private final String playerNickname;
    private final String gameName;

    public OnTokenAlreadyChosenMessage(String playerNickname, TokenColor token, ArrayList<TokenColor> tokenColors, String gameName) {
        errorMessage = token.toString() + " is already chosen.";
        this.availableTokenColors = tokenColors;
        this.playerNickname = playerNickname;
        this.gameName = gameName;
    }

    @Override
    public void notifyClient(ClientInterface client) throws RemoteException {
        client.sendMessageFromServer(this);
    }

    @Override
    public void methodToCall(View view) {
        view.exceptionHandler(this.playerNickname, this);
    }

    @Override
    public void methodToRecall(View view) {
        try {
            view.chooseTokenSetupPhase(0, 0, availableTokenColors, this.gameName);
        } catch (GenericException e) {
            throw new RuntimeException(e);
        }
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
