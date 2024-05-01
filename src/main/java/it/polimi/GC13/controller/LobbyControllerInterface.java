package it.polimi.GC13.controller;

import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;

public interface LobbyControllerInterface {
    void addPlayerToGame (ClientInterface client, Player player, int playersNumber, String gameName) throws IOException;
    void checkForExistingGame(ClientInterface client);
}
