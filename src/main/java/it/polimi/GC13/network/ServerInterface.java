package it.polimi.GC13.network;

import it.polimi.GC13.model.Player;

public interface ServerInterface {
    //methods that can be called by the client
    void addPlayerToGame(Player player);

    void quitGame();

    void writeMessage();//writes message in gamechat

    void drawCard();

}
