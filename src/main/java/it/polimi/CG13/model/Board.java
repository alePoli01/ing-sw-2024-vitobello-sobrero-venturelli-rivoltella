package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;
import it.polimi.CG13.exception.CardNotPlacedException;
import it.polimi.CG13.exception.EdgeNotFreeException;
import it.polimi.CG13.exception.NoResourceAvailableException;

import java.util.*;

public class Board {
    private Map<Coordinates, Cell> boardMap;
    private Player owner;               //owner of the board
    private int score;
    private EnumMap<ObjectType, Integer> objectsCollected;     //counter for each type of object present on the board
    private EnumMap<ReignType, Integer> reignsCollected;  //counter for each type of reigns present on the board
    private Set<Coordinates> availableCells;
    private Set<Coordinates> notAvailableCells;

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        this.score = 0;
        // populate map with 0 for each reign and object
        objectsCollected = new EnumMap<>(ObjectType.class);
        for (ObjectType object : ObjectType.values()) {
            objectsCollected.put(object, 0);
        }
        reignsCollected = new EnumMap<>(ReignType.class);
        for (ReignType reign : ReignType.values()) {
            reignsCollected.put(reign, 0);
        }
        availableCells = new HashSet<>();
        notAvailableCells = new HashSet<>();
    }

    public Map<Coordinates, Cell> getBoardMap() {
        return boardMap;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public Player getOwner() {
        return owner;
    }

    public void setOwner(Player owner) {
        this.owner = owner;
    }

    public EnumMap<ObjectType, Integer> getObjectsCollected() {
        return objectsCollected;
    }

    public EnumMap<ReignType, Integer> getReignsCollected() {
        return reignsCollected;
    }

    // check goldCard has enough resource to be played
    public void resourceVerifier(PlayableCard cardToPlace) throws NoResourceAvailableException {
        for (ReignType value : ReignType.values()) {
            if (reignsCollected.get(value) < cardToPlace.getResourceNeeded(value)) {
                throw new NoResourceAvailableException(value);
            }
        }
    }

    // call from the controller to verify it is possible to place the selected card
    public void isPossibleToPlace (Coordinates coordinates) throws EdgeNotFreeException {
        if (notAvailableCells.contains(coordinates)) {
            throw new EdgeNotFreeException(coordinates);
        }
    }

    // add card to the board and updates notAvailableCells and availableCells sets
    public void addCardToBoard(Coordinates xy, PlayableCard cardToPlace, boolean isFlipped) throws CardNotPlacedException {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new CardNotPlacedException(cardToPlace);
        }

        int i = 0;

        for (boolean edgeValue : cardToPlace.getLinkableEdge()) {
            Coordinates coordinateToCheck;
            switch (i) {
                case 0: // bottom-left
                    coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() - 1);
                    if (edgeValue) {
                        if (notAvailableCells.contains(coordinateToCheck)) {
                            availableCells.add(coordinateToCheck);
                        }
                    } else {
                        notAvailableCells.add(coordinateToCheck);
                    }
                case 1: // bottom-right
                    coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() - 1);
                    if (edgeValue) {
                        if (notAvailableCells.contains(coordinateToCheck)) {
                            availableCells.add(coordinateToCheck);
                        }
                    } else {
                        notAvailableCells.add(coordinateToCheck);
                    }
                case 2: // top-right
                    coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() + 1);
                    if (edgeValue) {
                        if (notAvailableCells.contains(coordinateToCheck)) {
                            availableCells.add(coordinateToCheck);
                        }
                    } else {
                        notAvailableCells.add(coordinateToCheck);
                    }
                case 3: // top-left
                    coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() + 1);
                    if (edgeValue) {
                        if (notAvailableCells.contains(coordinateToCheck)) {
                            availableCells.add(coordinateToCheck);
                        }
                    } else {
                        notAvailableCells.add(coordinateToCheck);
                    }
            }
                i++;
            }
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
            if (this.boardMap.containsKey(coordinateToCheck) && (!this.boardMap.get(coordinateToCheck).getIsFlipped()) || this.boardMap.get(coordinateToCheck).getCardPointer().getCardType().equals(CardType.STARTER)) {

                // determine if a new covered edge has reign or object and in case remove it from availableResources
                if (this.boardMap.get(coordinateToCheck).getCardPointer().edgeAvailable(counter)) {
                    for (ReignType element : this.boardMap.get(coordinateToCheck).getCardPointer().getReignPointEdge()) {
                        if (this.boardMap.get(coordinateToCheck).getCardPointer().getReignPointEdge(counter).equals(element)) {
                            reignsCollected.put(element, reignsCollected.get(element) - 1);
                        }
                    }

                    for (ObjectType element : this.boardMap.get(coordinateToCheck).getCardPointer().getObjectPointEdge()) {
                        if (this.boardMap.get(coordinateToCheck).getCardPointer().getObjectPointEdge(counter).equals(element)) {
                            objectsCollected.put(element, objectsCollected.get(element) - 1);
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
        if (!isFlipped) {
            // add card played reigns to the board
            for (boolean flag : cardToPlace.getLinkableEdge()) {
                if (flag) {
                    for (ReignType element : cardToPlace.getReignPointEdge()) {
                        reignsCollected.put(element, reignsCollected.get(element)+1);
                    }
                    for (ObjectType element : cardToPlace.getObjectPointEdge()) {
                        objectsCollected.put(element, objectsCollected.get(element)+1);
                    }
                }
            }
        } else {
            reignsCollected.put(cardToPlace.getReign(), reignsCollected.get(cardToPlace.getReign())+1);
        }
    }

}
