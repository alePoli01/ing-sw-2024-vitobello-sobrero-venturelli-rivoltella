package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.CardNotPlacedException;
import it.polimi.GC13.exception.EdgeNotFreeException;
import it.polimi.GC13.exception.NoResourceAvailableException;

import java.util.*;

public class Board {
    private Map<Coordinates, Cell> boardMap;
    private Player owner;               //owner of the board
    private int score;
    private final EnumMap<Resource, Integer> collectedResources;     //counter for each type of object present on the board
    private final Set<Coordinates> availableCells;
    private final Set<Coordinates> notAvailableCells;

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        this.score = 0;
        this.boardMap=new HashMap<>();
        // populate map with 0 for each reign and object
        collectedResources = new EnumMap<>(Resource.class);
        for (Resource resource : Resource.values()) {
            if (resource.isObject() || resource.isReign()) {
                collectedResources.put(resource, 0);
            }
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

    public EnumMap<Resource, Integer> getCollectedResources() {
        return this.collectedResources;
    }

    // check goldCard has enough resource to be played
    public void resourceVerifier(PlayableCard cardToPlace) throws NoResourceAvailableException {
        for (Resource reign : Resource.values()) {
            if (reign.isReign() && collectedResources.get(reign) < cardToPlace.resourceNeeded.get(reign)) {
                throw new NoResourceAvailableException(reign);
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
        // if statement for startCard placed in position 50,50 (board center)
        if (cardToPlace instanceof StartCard) {
            xy.setX(50);
            xy.setY(50);
            Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
            boardMap.put(xy, newCell);
            if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
                throw new CardNotPlacedException(cardToPlace);
            }
        } else {
            Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
            boardMap.put(xy, newCell);
            if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
                throw new CardNotPlacedException(cardToPlace);
            }

            int i = 0;

            // updates notAvailableCells and availableCells sets
            for (Resource edgeValue : cardToPlace.edgeResource) {
                Coordinates coordinateToCheck;
                switch (i) {
                    case 0: // bottom-left
                        coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() - 1);
                        if (!edgeValue.equals(Resource.NULL)) {
                            if (!notAvailableCells.contains(coordinateToCheck)) {  // if the coordinate isn't blocked by other cards, it is added to the availableCell set
                                availableCells.add(coordinateToCheck);
                            }
                        } else {
                            notAvailableCells.add(coordinateToCheck);
                        }
                    case 1: // bottom-right
                        coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() - 1);
                        if (!edgeValue.equals(Resource.NULL)) {
                            if (!notAvailableCells.contains(coordinateToCheck)) {
                                availableCells.add(coordinateToCheck);
                            }
                        } else {
                            notAvailableCells.add(coordinateToCheck);
                        }
                    case 2: // top-right
                        coordinateToCheck = new Coordinates(xy.getX() + 1, xy.getY() + 1);
                        if (!edgeValue.equals(Resource.NULL)) {
                            if (!notAvailableCells.contains(coordinateToCheck)) {
                                availableCells.add(coordinateToCheck);
                            }
                        } else {
                            notAvailableCells.add(coordinateToCheck);
                        }
                    case 3: // top-left
                        coordinateToCheck = new Coordinates(xy.getX() - 1, xy.getY() + 1);
                        if (!edgeValue.equals(Resource.NULL)) {
                            if (!notAvailableCells.contains(coordinateToCheck)) {
                                availableCells.add(coordinateToCheck);
                            }
                        } else {
                            notAvailableCells.add(coordinateToCheck);
                        }
                }
                i++;
            }
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
                for (Resource element : ((StartCard) cardToPlace).reignBackPointEdge) {
                    collectedResources.put(element, collectedResources.get(element) + 1);
                }
            } else {
                for (Resource element : ((StartCard) cardToPlace).frontReigns) {
                    collectedResources.put(element, collectedResources.get(element) + 1);
                }
            }
        } else {
            if (!isFlipped) {
                // add card played reigns to the board
                for (Resource resource : cardToPlace.edgeResource) {
                    if (!resource.isNullOrEmpty()) {
                        collectedResources.put(resource, collectedResources.get(resource) - 1);
                    }
                }
            } else {
                collectedResources.put(cardToPlace.reign, collectedResources.get(cardToPlace.reign) + 1);
            }
        }
    }
}
