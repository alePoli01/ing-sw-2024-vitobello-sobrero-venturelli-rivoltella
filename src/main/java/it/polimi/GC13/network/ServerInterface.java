package it.polimi.GC13.network;

public interface ServerInterface {
    //methods that can be called by the client
    void joinGame(String playerName);

    void quitGame();

    void writeMessage();//writes message in gamechat

    void drawCard();

}
