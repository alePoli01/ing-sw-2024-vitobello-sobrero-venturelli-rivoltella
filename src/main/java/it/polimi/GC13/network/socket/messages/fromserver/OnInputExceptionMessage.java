package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.exception.inputException.InputException;
import it.polimi.GC13.network.socket.ClientDispatcherInterface;


public class OnInputExceptionMessage implements MessagesFromServer {
    private final InputException exception;

    public OnInputExceptionMessage(InputException e) {
        this.exception = e;
    }

    public InputException getException() {
        return this.exception;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
