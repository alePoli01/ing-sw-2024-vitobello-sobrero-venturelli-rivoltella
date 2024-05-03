package it.polimi.GC13.network.socket.messages.fromserver.exceptions;

import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;
import it.polimi.GC13.view.View;


public interface OnInputExceptionMessage extends MessagesFromServer {

    void methodToRecall(View view);

    String getErrorMessage();

    String getPlayerNickname();
}
