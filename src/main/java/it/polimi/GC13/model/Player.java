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

/**
 * {@code Player} class represents a player in a game
 */
public class Player implements Serializable {
    private final String nickname;  //username of the player
    private TokenColor tokenColor; //token chosen by the Player
    private final ArrayList<PlayableCard> hand = new ArrayList<>(); //hand of the player ==> 3 cards max
    private final LinkedList<ObjectiveCard> privateObjectiveCard = new LinkedList<>(); // need an array to present 2 objective cards to the player
    private boolean myTurn; //true if it's the player turn
    private int turnPlayed = 0; //number of the current turn
    private Position position;  //position of the player (1-4)
    private Game game;

    /**
     * Creates a new {@code Player} with a specified nickname.
     *
     * @param nickname identifies the player in a game
     */

    public Player(String nickname) {
        this.nickname = nickname;
    }

    /**
     * Gets the nickname of the player.
     *
     * @return the nickname of the player.
     */
    public String getNickname() {
        return this.nickname;
    }

    /**
     * Checks if it is the player's turn.
     *
     * @return true if it is the player's turn, false otherwise.
     */
    public boolean isMyTurn() {
        return this.myTurn;
    }

    /**
     * Sets the token color for the player.
     *
     * @param tokenColor the token color to be set.
     * @throws GenericException if the token color is already chosen.
     */
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
                throw new GenericException(tokenColor + " already chosen");
            }
            this.game.getObserver().notifyClients(new OnTokenChoiceMessage(this.getNickname(), this.tokenColor));
        }
    }

    /**
     * Gets the token color of the player.
     *
     * @return the token color of the player.
     */
    public TokenColor getTokenColor() {
        return tokenColor;
    }

    /**
     * Gets the position of the player.
     *
     * @return the position of the player.
     */
    public Position getPosition() {
        return this.position;
    }

    /**
     * Sets the game for the player.
     *
     * @param game the game to be set.
     */
    public void setGame(Game game) {
        this.game = game;
    }

    /**
     * Gets the private objective cards of the player.
     *
     * @return a linked list of the player's private objective cards.
     */
    public LinkedList<ObjectiveCard> getPrivateObjectiveCard() {
        return this.privateObjectiveCard;
    }

    /**
     * Sets the position of the player.
     *
     * @param position the position to be set.
     */
    public void setPosition(Position position) {
        this.position = position;
    }

    /**
     * Gets the number of turns played by the player.
     *
     * @return the number of turns played by the player.
     */
    public int getTurnPlayed() {
        return this.turnPlayed;
    }

    /**
     * Increases the number of turns played by the player by one.
     */
    public void increaseTurnPlayed() {
        this.turnPlayed += 1;
    }

    /**
     * Gets the game of the player.
     *
     * @return the game of the player.
     */
    public Game getGame() {
        return this.game;
    }

    /**
     * Gets the board of the player.
     *
     * @return the board of the player.
     */
    public Board getBoard() {
        return this.game.getTable().getPlayerBoardMap().get(this);
    }

    /**
     * Gets the table of the game.
     *
     * @return the table of the game.
     */
    public Table getTable() {
        return this.game.getTable();
    }

    /**
     * Gets the hand of the player.
     *
     * @return an array list of the player's hand.
     */
    public ArrayList<PlayableCard> getHand() {
        return this.hand;
    }

    /**
     * Gets the serial numbers of the cards in the player's hand.
     *
     * @return a linked list of the serial numbers of the cards in the player's hand.
     */
    private LinkedList<Integer> getHandSerialNumber() {
        LinkedList<Integer> handSerialNumber = new LinkedList<>();
        this.hand.forEach((card) -> handSerialNumber.add(card.serialNumber));

        // DEBUG
        System.out.println(this.nickname+"'s hand: " + handSerialNumber);

        return handSerialNumber;
    }

    /**
     * Sets whether it is the player's turn.
     *
     * @param myTurn true if it is the player's turn, false otherwise.
     */
    public void setMyTurn(boolean myTurn) {
        this.myTurn = myTurn;
        System.out.println(this.nickname + " turn updated to " + this.myTurn);
        this.game.getObserver().notifyClients(new OnTurnUpdateMessage(this.getNickname(), myTurn));
    }

    /**
     * Checks if it is the player's turn.
     *
     * @throws GenericException if it is not the player's turn.
     */
    // check it is player's turn before playing
    public void checkMyTurn() throws GenericException {
        if (!myTurn) {
            throw new GenericException("It's not" + this.nickname + "turn.");
        }
    }

    /**
     * Removes a card from the player's hand after it is placed on the board.
     *
     * @param placedCard the card to be removed from the hand.
     * @throws GenericException if the card is not successfully removed.
     */
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

    /**
     * Adds drawn cards to the player's hand.
     *
     * @param drawnCard the cards to be added to the hand.
     * @throws GenericException if the cards are not successfully added.
     */
    // add drawnCard to the hand
    public void addToHand(List<PlayableCard> drawnCard) throws GenericException {
        this.hand.addAll(drawnCard);
        if (!this.hand.containsAll(drawnCard)) {
            throw new GenericException("Error updating " + this.nickname + "'s hand");
        }
        // send message to listener
        this.game.getObserver().notifyClients(new OnHandUpdate(this.nickname, this.getHandSerialNumber()));
    }

    /**
     * Chooses a private objective card for the game.
     *
     * @param serialPrivateObjectiveCard the serial number of the chosen private objective card.
     * @param readyPlayers the number of players ready.
     * @throws GenericException if the private objective card is not in the player's hand or already chosen.
     */
    // chose private objective card for the game
    public void setPrivateObjectiveCard(int serialPrivateObjectiveCard, int readyPlayers) throws GenericException {
        if (this.privateObjectiveCard.size() == 2) {
            if(this.privateObjectiveCard.stream().anyMatch(c -> c.serialNumber == serialPrivateObjectiveCard)) {
                //the player has 2 objective card, remove the one that wasn't chosen
                this.privateObjectiveCard.removeIf(objectiveCard -> objectiveCard.serialNumber != serialPrivateObjectiveCard);
                this.game.getObserver().notifyClients(new OnChoosePrivateObjectiveCardMessage(this.nickname, serialPrivateObjectiveCard, readyPlayers, this.game.numPlayer));
            }else{
                throw new GenericException("Private objective chosen wasn't in "+this.nickname+"'s hand");
            }
        } else {
            throw new GenericException("Private objective already chosen");
        }
    }

    /**
     * Gets the serial numbers of the player's private objective cards.
     *
     * @return a linked list of the serial numbers of the player's private objective cards.
     */
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
