package it.polimi.GC13.view;

import it.polimi.GC13.network.socket.messages.fromserver.OnCheckForExistingGameMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlayerAddedToGameMessage;

import java.io.IOException;

public interface View {
    void display() throws IOException;

    void display(OnCheckForExistingGameMessage onCheckForExistingGameMessage, int waitingPlayers) throws IOException;

    void update(OnPlayerAddedToGameMessage onPlayerAddedToGameMessage, int waitingPlayers) throws IOException;
}
