package it.polimi.GC13.network;

import it.polimi.GC13.network.ClientInterface;

public interface LostConnectionToClientInterface {
    //interfaccia usata per chiamare il metodo che gestisce cosa fare quando si perde la connessione col client
    void connectionLost(ClientInterface client);
}
