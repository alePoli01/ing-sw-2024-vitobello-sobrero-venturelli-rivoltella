package it.polimi.GC13.controller;

import it.polimi.GC13.exception.NicknameAlreadyTakenException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.Player;
import it.polimi.GC13.network.ClientInterface;

import java.io.IOException;

public interface LobbyControllerInterface {
    void addPlayerToGame (ClientInterface client, Player player, int playersNumber) throws IOException, PlayerNotAddedException, NicknameAlreadyTakenException;
    void checkForExistingGame(ClientInterface client);
}
