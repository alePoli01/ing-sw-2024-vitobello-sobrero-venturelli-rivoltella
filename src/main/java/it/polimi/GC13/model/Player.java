package it.polimi.GC13.model;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.network.socket.messages.fromserver.OnChoosePrivateObjectiveCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnTokenColorChooseMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnTurnUpdateMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnTokenAlreadyChosenMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;

public class Player implements Serializable {
    private final String nickname;  //username of the player
    private TokenColor tokenColor; //token chosen by the Player
    private final ArrayList<PlayableCard> hand; //hand of the player ==> 3 cards max
    private final LinkedList<ObjectiveCard> privateObjectiveCard; // need an array to present 2 objective cards to the player
    private boolean myTurn; //true if it's the player turn
    private int turnPlayed; //number of the current turn
    private Position position;  //position of the player (1-4)
    private Game game;


    //at the creation each player has only its nickname, everything else is defined in the setup phase
    public Player(String nickname) {
        this.nickname = nickname;
        this.turnPlayed = 0;
        this.myTurn = false;
        this.hand = new ArrayList<>();
        this.privateObjectiveCard = new LinkedList<>();
    }

    public String getNickname() {
        return this.nickname;
    }

    public TokenColor getTokenColor() {
        return this.tokenColor;
    }

    public void setTokenColor(TokenColor tokenColor) throws GenericException {
        if (this.tokenColor == null) {
            if (this.getTable().getTokenColors().contains(tokenColor)) {
                //color can be taken, assign it to this and remove it from take-able colors
                this.tokenColor = tokenColor;
                this.getTable().getTokenColors().remove(tokenColor);
            } else {
                //case: color already taken
                ArrayList<TokenColor> tokenColorsList = new ArrayList<>();
                for (TokenColor tc : TokenColor.values()) {
                    if (this.getGame().getTable().getTokenColors().contains(tc)) {
                        tokenColorsList.add(tc);
                    }
                }
                this.game.getObserver().notifyClients(new OnTokenAlreadyChosenMessage(this.getNickname(), tokenColor, tokenColorsList));
                throw new GenericException(tokenColor + " already chose");
            }
            this.game.getObserver().notifyClients(new OnTokenColorChooseMessage(this.getNickname(), this.tokenColor));
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LinkedList<ObjectiveCard> getPrivateObjectiveCard() {
        return privateObjectiveCard;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getTurnPlayed() {
        return turnPlayed;
    }

    public void increaseTurnPlayed() {
        this.turnPlayed += 1;
    }

    public Game getGame() {
        return game;
    }

    public Board getBoard() {
        return this.game.getTable().getPlayerBoardMap().get(this);
    }

    public Table getTable() {
        return this.game.getTable();
    }

    public ArrayList<PlayableCard> getHand() {
        return this.hand;
    }

    public int[] getHandCardSerialNumber() {
        // Initialize the array to store serial numbers
        int[] handCardSerialNumber = new int[this.hand.size()];

        // Fill the array with serial numbers of cards in the hand
        for (int i = 0; i < this.hand.size(); i++) {
            handCardSerialNumber[i] = this.hand.get(i).serialNumber;
        }

        // Return the array of serial numbers
        return handCardSerialNumber;
    }

    public int[] getPrivateObjectiveCardSerialNumber() {
        int[] privateObjectiveCardSerialNumber = new int[this.privateObjectiveCard.size()];
        for (int i = 0; i < this.privateObjectiveCard.size(); i++) {
            privateObjectiveCardSerialNumber[i] = this.privateObjectiveCard.get(i).serialNumber;
        }
        return privateObjectiveCardSerialNumber;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        this.game.getObserver().notifyClients(new OnTurnUpdateMessage(this.getNickname(), myTurn));
    }

    // check it is player's turn before playing
    public void checkMyTurn() throws NotMyTurnException {
        if (!myTurn) {
            throw new NotMyTurnException(this.getNickname());
        }
    }

    // remove placedCard after it is placed on the board
    public void handUpdate(PlayableCard placedCard) throws GenericException {
        hand.remove(placedCard);
        if (hand.contains(placedCard)) {
            throw new GenericException("Error model" + placedCard.serialNumber +  "not placed.");
        }
    }

    // add drawnCard to the hand
    public void addToHand(PlayableCard drawnCard) throws GenericException {
        hand.add(drawnCard);
        if (!hand.contains(drawnCard)) {
            throw new GenericException(drawnCard.serialNumber + "not added to the hand");
        }
    }

    // chose private objective card for the game
    public void setPrivateObjectiveCard(int indexPrivateObjectiveCard, int readyPlayers) throws GenericException {
        if (this.privateObjectiveCard.size() == 2) {
            if (indexPrivateObjectiveCard == 0) {
                this.privateObjectiveCard.removeLast();
            } else {
                this.privateObjectiveCard.removeFirst();
            }
            this.game.getObserver().notifyClients(new OnChoosePrivateObjectiveCardMessage(this.nickname, indexPrivateObjectiveCard, readyPlayers, this.game.numPlayer));
        } else {
            throw new GenericException("Private objective already chose");
        }
    }
}
