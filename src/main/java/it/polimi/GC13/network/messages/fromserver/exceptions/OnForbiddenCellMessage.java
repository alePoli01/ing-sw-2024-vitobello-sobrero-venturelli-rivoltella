package it.polimi.GC13.network.messages.fromserver.exceptions;

import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.view.View;
import java.rmi.RemoteException;
import java.util.LinkedList;
import java.util.List;

public class OnForbiddenCellMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final List<Coordinates> availableCells = new LinkedList<>();
    private final String playerNickname;

    public OnForbiddenCellMessage(String playerNickname, int X, int Y, List<Coordinates> availableCells) {
        errorMessage = "It's not possible to place cards in: (" + X + ", " + Y + ")";
        this.availableCells.addAll(availableCells);
        this.playerNickname = playerNickname;
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
        view.displayAvailableCells(this.availableCells);
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