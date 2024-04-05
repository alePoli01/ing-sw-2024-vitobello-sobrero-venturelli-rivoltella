package it.polimi.GC13.model;

import it.polimi.GC13.controller.LobbyController;

public class Server {
    private final LobbyController lobbyController;

    public Server() {
        this.lobbyController = new LobbyController();
    }

    public LobbyController getLobbyController() {
        return lobbyController;
    }
}
