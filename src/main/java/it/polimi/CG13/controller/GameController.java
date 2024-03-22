package it.polimi.CG13.controller;

import it.polimi.CG13.enums.FirstCardsNotGivenException;
import it.polimi.CG13.enums.GameState;
import it.polimi.CG13.exception.CardNotAddedToHandException;
import it.polimi.CG13.exception.StartCardNotGivenException;
import it.polimi.CG13.model.*;

public class GameController {
    private Server serverId;

    /* TODO
        capire come il game parte e viene settato a valido, ovvero c'è il numero di giocatori prestabilito
     */

    public void addPlayerToGame(Server serverId, Game game, Player player) {
        if (game.getGameState().equals(GameState.SETUP)) {
            serverId.addPlayerToGame(player, game);
        }
    }

    /* TODO
        capire come aspettare che il giocatore posizioni la carta start per andare avanti
     */
    public void startGame(Game game) {
        try {
            game.getDeck().shuffleDecks();
            game.giveStartCard();
        } catch (StartCardNotGivenException e) {
            System.out.println(e.getMessage());
        }
    }

    // update gameStatus given current status
    public void updateGameStatus(Game game, GameState currentGameStatus) {
        switch (currentGameStatus) {
            case SETUP -> game.setGameState(GameState.DEALING_PHASE);
            case DEALING_PHASE -> game.setGameState(GameState.START);
        }
    }

    // second game phase, after all players placed start card
    public void startGame2(Game game) throws CardNotAddedToHandException {
        if (game.getGameState().equals(GameState.DEALING_PHASE)) {
            try {
                game.giveFirstCards();
                game.setCommonObjectiveCard();
            } catch (FirstCardsNotGivenException e) {
                System.out.println(e.getMessage());
            }
        }
    }
}
