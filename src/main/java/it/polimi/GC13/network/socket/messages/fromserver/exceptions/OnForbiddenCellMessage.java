package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.model.Coordinates;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;
import it.polimi.GC13.view.View;


import java.util.HashSet;
import java.util.Set;

public class OnForbiddenCellMessage implements OnInputExceptionMessage {
    private final String errorMessage;
    private final Set<Coordinates> availableCells = new HashSet<>();
    private final String playerNickname;

    public OnForbiddenCellMessage(String playerNickname, int X, int Y, Set<Coordinates> availableCells) {
        errorMessage = "It's not possible to place cards in: (%s, %s)" + X + Y;
        this.availableCells.addAll(availableCells);
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