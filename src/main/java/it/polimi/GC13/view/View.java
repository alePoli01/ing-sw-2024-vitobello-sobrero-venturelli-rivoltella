package it.polimi.GC13.view;

import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.io.IOException;

public interface View {
    public void display() throws IOException;

    public void display(OnCheckForExistingGameMessage onCheckForExistingGameMessage, boolean noExistingGame) throws IOException;

    void update(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage);
}
