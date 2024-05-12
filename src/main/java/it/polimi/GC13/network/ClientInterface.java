package it.polimi.GC13.network;

import it.polimi.GC13.network.socket.ClientDispatcher;
import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;

import java.io.IOException;
import java.io.Serial;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;
import java.rmi.RemoteException;

public interface ClientInterface extends Remote {

    // Generic method to send messages from server to client after the game is started
    void sendMessage(MessagesFromServer message) throws RemoteException;
}