package it.polimi.GC13.network.socket;

import it.polimi.GC13.network.ClientInterface;
import it.polimi.GC13.network.socket.messages.fromclient.*;

import java.io.IOException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ServerDispatcherInterface extends Remote {

    void sendToControllerDispatcher(MessagesFromClient messagesFromClient, ClientInterface client) throws RemoteException;
}
