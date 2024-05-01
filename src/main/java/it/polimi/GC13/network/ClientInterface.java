package it.polimi.GC13.network;

import it.polimi.GC13.network.socket.messages.fromserver.MessagesFromServer;

import java.io.IOException;
import java.io.Serializable;
import java.rmi.NotBoundException;
import java.rmi.Remote;

public interface ClientInterface extends Remote, Serializable {

    // Generic method to send messages from server to client after the game is started
    void sendMessage(MessagesFromServer message);

    void startRMIConnection() throws IOException, NotBoundException;

    void poke() throws IOException;
}