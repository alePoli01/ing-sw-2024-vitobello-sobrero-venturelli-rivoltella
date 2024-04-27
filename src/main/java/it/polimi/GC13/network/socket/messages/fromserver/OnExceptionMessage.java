package it.polimi.GC13.network.socket.messages.fromserver;

import it.polimi.GC13.network.socket.ClientDispatcherInterface;


public class OnExceptionMessage implements MessagesFromServer {
    private final Exception exception;

    public OnExceptionMessage(Exception e) {
        this.exception = e;
    }

    public Exception getException() {
        return this.exception;
    }

    @Override
    public void dispatch(ClientDispatcherInterface clientDispatcher) {
        clientDispatcher.dispatch(this);
    }
}
