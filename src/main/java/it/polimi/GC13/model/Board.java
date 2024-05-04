package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceStartCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnForbiddenCellMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnNotEnoughResourceToPlaceMessage;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Board implements Serializable {
    private final Map<Coordinates, Cell> boardMap = new HashMap<>();
    private final Player owner;               //owner of the board
    private int playerScore = 0;
    private final EnumMap<Resource, Integer> collectedResources = new EnumMap<>(Resource.class);;     //counter for each type of object present on the board
    private final Set<Coordinates> availableCells = new HashSet<>();
    private final Set<Coordinates> notAvailableCells = new HashSet<>();   // -> used to not add available cell

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        // populate map with 0 for each reign and object
        for (Resource resource : Resource.values()) {
            if (resource.isObject() || resource.isReign()) {
                collectedResources.put(resource, 0);
            }
        }
    }

    public Map<Coordinates, Cell> getBoardMap() {
        return boardMap;
    }

    public Set<Coordinates> getAvailableCells() {
        return this.availableCells;
    }

    public int getPlayerScore() {
        return playerScore;
    }

    public void setPlayerScore(int playerScore) {
        this.playerScore = playerScore;
    }

    public Player getOwner() {
        return owner;
    }

    public EnumMap<Resource, Integer> getCollectedResources() {
        return this.collectedResources;
    }

    // check goldCard has enough resource to be played
    public void resourceVerifier(PlayableCard cardToPlace) throws GenericException {
        for (Resource reign : Resource.values()) {
            if (reign.isReign() && collectedResources.get(reign) < cardToPlace.resourceNeeded.get(reign)) {
                this.owner.getGame().getObserver().notifyClients(new OnNotEnoughResourceToPlaceMessage(this.owner.getNickname(), reign));
                throw new GenericException("Not enough resource to place " + reign);
            }
        }
    }

    // call from the controller to verify it is possible to place the selected card
    public Coordinates isPossibleToPlace(int X, int Y) throws GenericException {
        Coordinates xy = availableCells.stream()
                .filter(coordinates -> coordinates.getX() == X && coordinates.getY() == Y)
                .findFirst()
                .orElse(null);

        if (xy == null) {
            owner.getGame().getObserver().notifyClients(new OnForbiddenCellMessage(owner.getNickname(), X, Y, availableCells));
            throw new GenericException("Forbidden cell " + X + Y);
        }
        return xy;
    }

    public void addStartCardToBoard(PlayableCard cardToPlace, boolean isFlipped) throws GenericException {
        Coordinates xy = new Coordinates(50, 50);
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new GenericException("Sever didn't update the Board");
        } else {
            this.owner.getHand().remove(cardToPlace);
            this.owner.getGame().getObserver().notifyClients(new OnPlaceStartCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped));
        }
        this.updateAvailableCells(cardToPlace, xy);
    }

    // add card to the board
    public void addCardToBoard(Coordinates xy, PlayableCard cardToPlace, boolean isFlipped) throws GenericException {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new GenericException("Sever didn't update the Board");
        }
        this.owner.getHand().remove(cardToPlace);
        this.updateAvailableCells(cardToPlace, xy);
        this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped));
    }

    // updates notAvailableCells and availableCells sets
    private void updateAvailableCells(PlayableCard cardPlaced, Coordinates xy) {
        List<Coordinates> offset = new LinkedList<>();
        offset.add(new Coordinates(-1, -1));
        offset.add(new Coordinates(1, -1));
        offset.add(new Coordinates(1, 1));
        offset.add(new Coordinates(-1, 1));

        int i = 0;

        while (i < cardPlaced.edgeResource.length) {
            Resource resource = cardPlaced.edgeResource[i];
            Coordinates coordinatesToCheck = new Coordinates(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY());
            if (resource != Resource.NULL && !notAvailableCells.contains(coordinatesToCheck)) {
                this.availableCells.add(coordinatesToCheck);
                System.out.println("New available coordinates: (" + coordinatesToCheck.getX() + ", " + coordinatesToCheck.getY() + ")");
            } else {
                notAvailableCells.add(coordinatesToCheck);
            }
            i++;
        }
    }

    // method used to cycle on surrounding coordinate (atm used only to count gold card given points)
    public int surroundingCardsNumber(Coordinates xy) {
        int counter = 0;
        for (int i = 0; i < 4; i++) {
            Coordinates coordinateToCheck;
            switch (i) {
                case 0: // bottom-left
                    coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() - 1);
                    if (this.getBoardMap().containsKey(coordinateToCheck)) {
                        counter++;
                    }
                case 1: // bottom-left
                    coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() - 1);
                    if (this.getBoardMap().containsKey(coordinateToCheck)) {
                        counter++;
                    }
                case 2: // top-right
                    coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() + 1);
                    if (this.getBoardMap().containsKey(coordinateToCheck)) {
                        counter++;
                    }
                case 3: // top-left
                    coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() + 1);
                    if (this.getBoardMap().containsKey(coordinateToCheck)) {
                        counter++;;
                    }
            }
        }
        return counter;
    }

    // update surrounding cards edges
    public void removeResources(Coordinates coordinates) {
        int counter = 0;
        int myCardEdge = 2;

        int[][] relativeCoordinates = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] offset : relativeCoordinates) {
            // Calculate coordinates to check
            Coordinates coordinateToCheck = new Coordinates(coordinates.getX() + offset[0], coordinates.getY() + offset[1]);

            // Check if the coordinate exists in the Map
            if (this.boardMap.containsKey(coordinateToCheck) && (!this.boardMap.get(coordinateToCheck).isFlipped) || this.boardMap.get(coordinateToCheck).getCardPointer().cardType.equals(CardType.STARTER)) {

                // determine if a new covered edge has reign or object and in case remove it from availableResources
                if (!(this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[counter].isNullOrEmpty())) {
                    for (Resource resource : this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource) {
                        if (this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[counter].equals(resource)) {
                            if (resource.isReign() || resource.isObject()) {
                                collectedResources.put(resource, collectedResources.get(resource) - 1);
                            }
                        }
                    }
                }
            }
            counter++;
            if (myCardEdge == 3) {
                myCardEdge = 0;
            } else {
                myCardEdge++;
            }
        }
    }

    // simplified for cycles to update reigns and objects
    public void addResource(PlayableCard cardToPlace, boolean isFlipped) {
        // if statement for start card, else for gold / resources
        if (cardToPlace instanceof StartCard) {
            if (isFlipped) {
                Arrays.stream(((StartCard) cardToPlace).reignBackPointEdge)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            } else {
                Arrays.stream(((StartCard) cardToPlace).frontReigns)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            }
        } else {
            if (!isFlipped) {
                // add card played resource to the board
                Arrays.stream(cardToPlace.edgeResource)
                        .filter(resource -> resource.isObject() || resource.isReign())
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            } else {
                collectedResources.put(cardToPlace.reign, collectedResources.get(cardToPlace.reign) + 1);
            }
        }
    }

    public boolean containsKeyOfValue(int x, int y){
        for(Coordinates xy : this.getBoardMap().keySet()){
            if(xy.getX()==x && xy.getY()==y){
                return true;
            }
        }
        return false;
    }

    public Coordinates get(int x,int y){
        for(Coordinates xy : this.getBoardMap().keySet()){
            if(xy.getX()==x && xy.getY()==y){
                return xy;
            }
        }
        return null;
    }
}
