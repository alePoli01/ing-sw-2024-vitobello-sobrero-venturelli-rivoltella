package it.polimi.GC13.network.socket;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.PlayerJoiningMessage;

import java.io.IOException;

public class ServerDispatcher implements ServerDispatcherInterface {
    private final ControllerDispatcher controllerDispatcher;

    public ServerDispatcher(ControllerDispatcher controllerDispatcher) {
        this.controllerDispatcher=controllerDispatcher;
    }

    @Override
    public void dispatch(PlayerJoiningMessage playerJoiningMessage, ClientInterface view) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException {
        //now the server knows what to do
        controllerDispatcher.addPlayerToGame(view, playerJoiningMessage.getPlayer());
    }
}
