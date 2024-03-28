package it.polimi.GC13.controller;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.PlayerNotAddedException;
import it.polimi.GC13.model.*;

public class GameController {
    private Server serverId;

    public void createNewGame(Player player, int playersNumber) {
        serverId.createGame(player, playersNumber);
    }

    public void addPlayerToGame(Game game, Player player) {
        try {
            game.addPlayerToGame(player);
            if (game.numPlayer == game.getCurrNumPlayer()) {
                game.updateGameStatus();
            }
        } catch (PlayerNotAddedException e) {
            System.out.println(e.getMessage());
        }
    }

    /* TODO
        capire come aspettare che il giocatore posizioni la carta start per andare avanti
     */

    // deal start card to all players in the selected game
    public void dealingPhase(Game game) {
        if (game.getGameState().equals(GameState.TABLE_SETUP)) {
            try {
                game.getDeck().shuffleDecks();
                game.getTable().tableSetup();
                game.giveStartCard();
                game.updateGameStatus();
            } catch (CardNotAddedToHandException e) {
                System.out.println(e.getMessage());
            }
        }
    }

    // second game phase, deal resource and gold cards to all players after start card is placed
    public void startGame(Game game) throws CardNotAddedToHandException {
        if (game.getGameState().equals(GameState.DEALING_PHASE)) {
            try {
                game.giveStartCard();
                game.giveFirstCards();
                game.setCommonObjectiveCards();
                game.givePrivateObjectiveCards();
                game.setPlayersPosition();
            } catch (CardNotAddedToHandException e){
                System.out.println(e.getMessage());
            }
        }
    }
}
