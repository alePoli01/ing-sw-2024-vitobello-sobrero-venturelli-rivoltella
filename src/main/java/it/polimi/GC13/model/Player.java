package it.polimi.GC13.model;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.enums.Position;
import it.polimi.GC13.exception.*;
import it.polimi.GC13.network.messages.fromserver.OnChoosePrivateObjectiveCardMessage;
import it.polimi.GC13.network.messages.fromserver.OnHandUpdate;
import it.polimi.GC13.network.messages.fromserver.OnTokenChoiceMessage;
import it.polimi.GC13.network.messages.fromserver.OnTurnUpdateMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnTokenAlreadyChosenMessage;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

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

    public boolean isMyTurn() {
        return this.myTurn;
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
                this.game.getObserver().notifyClients(new OnTokenAlreadyChosenMessage(this.getNickname(), tokenColor, tokenColorsList, this.game.getGameName()));
                throw new GenericException(tokenColor + " already chose");
            }
            this.game.getObserver().notifyClients(new OnTokenChoiceMessage(this.getNickname(), this.tokenColor));
        }
    }

    public Position getPosition() {
        return this.position;
    }

    public void setGame(Game game) {
        this.game = game;
    }

    public LinkedList<ObjectiveCard> getPrivateObjectiveCard() {
        return this.privateObjectiveCard;
    }

    public void setPosition(Position position) {
        this.position = position;
    }

    public int getTurnPlayed() {
        return this.turnPlayed;
    }

    public void increaseTurnPlayed() {
        this.turnPlayed += 1;
    }

    public Game getGame() {
        return this.game;
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

    private LinkedList<Integer> getHandSerialNumber() {
        LinkedList<Integer> handSerialNumber = new LinkedList<>();
        this.hand.forEach((card) -> handSerialNumber.add(card.serialNumber));

        // DEBUG
        System.out.println(this.nickname);
        handSerialNumber.forEach(System.out::println);

        return handSerialNumber;
    }

    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        System.out.println(this.nickname + " turn updated to " + this.myTurn);
        this.game.getObserver().notifyClients(new OnTurnUpdateMessage(this.getNickname(), myTurn));
    }

    // check it is player's turn before playing
    public void checkMyTurn() throws GenericException {
        if (!myTurn) {
            throw new GenericException("It's not" + this.nickname + "turn.");
        }
    }

    // remove placedCard after it is placed on the board
    public void removeFromHand(PlayableCard placedCard) throws GenericException {
        this.hand.remove(placedCard);
        System.out.println("Card " + placedCard.serialNumber + " was removed");
        if (hand.contains(placedCard)) {
            throw new GenericException("Error model" + placedCard.serialNumber +  "not placed.");
        }
        // send message to listener
        this.game.getObserver().notifyClients(new OnHandUpdate(this.nickname, this.getHandSerialNumber()));
    }

    // add drawnCard to the hand
    public void addToHand(List<PlayableCard> drawnCard) throws GenericException {
        this.hand.addAll(drawnCard);
        if (!this.hand.containsAll(drawnCard)) {
            throw new GenericException("Error updating " + this.nickname + "'s hand");
        }
        // send message to listener
        this.game.getObserver().notifyClients(new OnHandUpdate(this.nickname, this.getHandSerialNumber()));
    }

    // chose private objective card for the game
    public void setPrivateObjectiveCard(int serialPrivateObjectiveCard, int readyPlayers) throws GenericException {
        if (this.privateObjectiveCard.size() == 2) {
            this.privateObjectiveCard.removeIf(objectiveCard -> objectiveCard.serialNumber != serialPrivateObjectiveCard);
            this.game.getObserver().notifyClients(new OnChoosePrivateObjectiveCardMessage(this.nickname, serialPrivateObjectiveCard, readyPlayers, this.game.numPlayer));
        } else {
            throw new GenericException("Private objective already chosen");
        }
    }

    public LinkedList<Integer> getPrivateObjectiveCardSerialNumber() {
        LinkedList<Integer> privateObjectiveCardSerialNumber = new LinkedList<>();
        this.privateObjectiveCard
                .forEach(objectiveCard -> privateObjectiveCardSerialNumber.add(objectiveCard.serialNumber));
        return privateObjectiveCardSerialNumber;
    }

    /**
     * @return returns player's score from the players score map in Table
     */
    public Integer getScore() {
        return this.getTable().getPlayersScore().get(this);
    }
}
