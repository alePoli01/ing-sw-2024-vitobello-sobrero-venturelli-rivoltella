package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;
import it.polimi.CG13.exception.CardNotPlaced;
import it.polimi.CG13.exception.EdgeNotFree;
import it.polimi.CG13.exception.NoResourceAvailable;

import java.util.Map;

public class Board {
    private Map<ReignType, Integer> reignMissing;     // reignMissign to place goldCard
    private Map<Coordinates, Cell> boardMap;
    private Player owner;               //owner of the board
    private int score;
    private Map<ObjectType, Integer> objectsCollected;     //counter for each type of object present on the board
    private Map<ReignType, Integer> reignsCollected;      //counter for each type of reigns present on the board

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        this.score = 0;
        // populate map with 0 for each reign and object
        for (ReignType value : ReignType.values()) {
            reignsCollected.put(value, 0);
        }
        for (ObjectType value : ObjectType.values()) {
            objectsCollected.put(value, 0);
        }
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

    // check goldCard has enough resource to be played
    public void resourceVerifier(PlayableCard cardToPlace) throws NoResourceAvailable {
        for (ReignType value : ReignType.values()) {
            if (reignsCollected.get(value) < cardToPlace.getResourceNeeded(value)) {
                throw new NoResourceAvailable(value);
            }
        }
    }

    // call from the controller to verify it is possible to place the selected card
    public void isPossibleToPlace(PlayableCard cardToPlace, Coordinates coordinates, int targetCardEdge) throws EdgeNotFree {

        int cardToPlaceEdge; // eventualmente si può mettere un cath exception anche qui, soprattutto da cli visto che target edge è messo a mano (come tutto il resto)

        if (targetCardEdge == 0 || targetCardEdge == 1) {
            cardToPlaceEdge = targetCardEdge + 2;
        } else {
            cardToPlaceEdge = targetCardEdge - 2;
        }

        // check that myCard has an actual availableEdge
        if (!cardToPlace.edgeAvailable(cardToPlaceEdge)) {
            throw new EdgeNotFree(cardToPlace, cardToPlaceEdge);
        }

        // check that targetCard has an availableEdge in targetCardEdge
        PlayableCard targetCard = this.boardMap.get(coordinates).getCardPointer();
        if (!targetCard.edgeAvailable(targetCardEdge)) {
            throw new EdgeNotFree(targetCard, targetCardEdge);
        }

        // check spaceAround is free

        // Define the relative coordinates to check (top-right, top-left, bottom-right, bottom-left)
        int[][] relativeCoordinates = {{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        for (int[] offset : relativeCoordinates) {
            // Calculate coordinates to check
            Coordinates coordinateToCheck = new Coordinates(coordinates.getX() + offset[0], coordinates.getY() + offset[1]);

            // Check if the coordinate exists in the Map
            if (this.boardMap.containsKey(coordinateToCheck)) {
                PlayableCard spaceToCheck = this.boardMap.get(coordinateToCheck).getCardPointer();
                // Check if the edge is not free
                if (!spaceToCheck.edgeAvailable(targetCardEdge)) {
                    throw new EdgeNotFree(targetCard, targetCardEdge);
                }
            }
        }
    }

    // add card to the board
    public void addCardToBoard(Coordinates xy, PlayableCard cardToPlace) throws CardNotPlaced {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed());
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new CardNotPlaced(cardToPlace);
        }
    }

    // update surrounding cards edges
    public void edgesUpdate(Coordinates coordinates) {
        int counter = 0;
        int myCardEdge = 2;

        int[][] relativeCoordinates = {{1, 1}, {-1, 1}, {-1, -1}, {1, -1}};

        for (int[] offset : relativeCoordinates) {
            // Calculate coordinates to check
            Coordinates coordinateToCheck = new Coordinates(coordinates.getX() + offset[0], coordinates.getY() + offset[1]);

            // Check if the coordinate exists in the Map
            if (this.boardMap.containsKey(coordinateToCheck)) {

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

                    this.boardMap.get(coordinateToCheck).getCardPointer().setLinkableEdge(false, counter);
                    this.boardMap.get(coordinateToCheck).getCardPointer().setLinkableEdge(false, myCardEdge);
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
