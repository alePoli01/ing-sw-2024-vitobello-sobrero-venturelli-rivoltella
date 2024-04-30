package it.polimi.GC13.controller.gameStateController;

import it.polimi.GC13.enums.GameState;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.exception.CardNotAddedToHandException;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.exception.inputException.PlayerNotAddedException;
import it.polimi.GC13.exception.inputException.TokenAlreadyChosenException;
import it.polimi.GC13.model.*;
import it.polimi.GC13.network.socket.messages.fromserver.OnDealingCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceStartCardMessage;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SetupPhase implements GamePhase {

    private final Controller controller;
    private final List<Player> playerList = new ArrayList<>();

    public SetupPhase(Controller controller) {
        this.controller = controller;
        this.prepareTable(this.controller.getGame());
    }
    
    private void prepareTable(Game game) {
        try {
            game.getTable().tableSetup();
            game.giveStartCard();
            // adds to the TUI handsCard
            for (Player player : playerList) {
                this.controller.notifySpecificClients(new OnDealingCardMessage(player.getHandCardSerialNumber()), List.of(player));
            }
        } catch (CardNotAddedToHandException e){
            System.out.println(e.getMessage());
        }
    }

    public void placeStartCard(Player player, boolean isFlipped) {
        try {
            System.out.println("I am placing the card");
            PlayableCard cardToPlace = player.getHand().getFirst();
            player.getBoard().addCardToBoard(new Coordinates(50, 50), cardToPlace, isFlipped);
            player.getBoard().addResource(cardToPlace, isFlipped);
            // if all Players positioned start card, it updates the controller
            if (playersPlacedStartCard(player)) {
                this.controller.updateController(new DealingPhase(this.controller));
                this.controller.getGame().setGameState(GameState.DEALING_CARDS);
            } else {
                this.playerList.add(player);
                this.controller.notifySpecificClients(new OnPlaceStartCardMessage(this.playerList.size(), this.controller.getGame().numPlayer, isFlipped), this.playerList);
            }
        } catch (CardNotPlacedException e) {
            System.out.println(e.getMessage());
        }
    }

    // check that all players in the same game positioned the start card
    private boolean playersPlacedStartCard(Player player) {
        for (Player p : player.getGame().getPlayerList()) {
            if (!p.getBoard().containsKeyOfValue(50, 50)) {
                return false;
            }
        }
        return true;
    }

    public void chooseToken(Player player, TokenColor tokenColor) throws TokenAlreadyChosenException {
        if (player.getToken() == null) {
            if (player.getGame().getTable().getTokenColors().contains(tokenColor)) {
                //color can be taken, assign it to player and remove it from take-able colors
                player.setToken(tokenColor);
                player.getTable().getTokenColors().remove(tokenColor);
            } else {
                //case: color already taken
                ArrayList<TokenColor> tokenColorsList = new ArrayList<>();
                for (TokenColor tc : TokenColor.values()) {
                    if (player.getGame().getTable().getTokenColors().contains(tc)) {
                        tokenColorsList.add(tc);
                    }
                }
                throw new TokenAlreadyChosenException(tokenColor, tokenColorsList);
            }
        }
    }


    public void placeCard(Player player, PlayableCard cardToPlace, boolean isFlipped, Coordinates xy) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void drawCard(Player player, Table table, PlayableCard cardToDraw) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void choosePrivateObjective(Player player, ObjectiveCard card) {
        System.out.println("Error, game is in" + this.controller.getGame().getGameState());
    }

    public void addPlayerToExistingGame(Player player, Game existingGame) throws PlayerNotAddedException {
        throw new PlayerNotAddedException(player);
    }
}
