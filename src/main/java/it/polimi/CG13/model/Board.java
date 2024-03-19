package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ReignType;
import it.polimi.CG13.exception.EdgeNotFree;
import it.polimi.CG13.exception.NoResourceAvailable;

import java.util.Arrays;
import java.util.Map;

public class Board {
    private ReignType reignMissing;     // reignMissign to place goldCard
    private Map<Coordinates, Cell> boardMap;
    private Player owner;               //owner of the board
    private int score;
    private int[] objectsCollected;     //counter for each type of object present on the board
    private int[] reignsCollected;      //counter for each type of reigns present on the board

    //initialize all the values to zero
    public Board() {
        this.owner = null;
        this.score = 0;
        objectsCollected = new int[3];
        reignsCollected = new int[4];
        Arrays.fill(objectsCollected, 0);
        Arrays.fill(objectsCollected, 0);
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

    public int getObjectsCollected(int position) {
        return objectsCollected[position];
    }

    public void updateObjectsCollected(int position) {
        this.objectsCollected[position] += 1;
    }

    public int getReignsCollected(int position) {
        return reignsCollected[position];
    }

    public void updateReignsCollected(int position) {
        this.reignsCollected[position] += 1;
    }

    public void resourceVerifier(PlayableCard cardToPlace) throws NoResourceAvailable {
        for (int position = 0; position < 4; position++) {
            if (cardToPlace.getResourceNeeded(position) > getReignsCollected(position)) {
                reignMissing = reignMissing.correspondingReignType(position);
                throw new NoResourceAvailable(reignMissing);
            }
        }
    }

    public void isPossibleToPlace(PlayableCard cardToPlace, Coordinates coordinates, int targetCardEdge) throws EdgeNotFree {

        int cardToPlaceEdge; // eventualmente si può mettere un cath exception anche qui, soprattutto da cli visto che target edge è messo a mano (come tutto il resto)

        if (targetCardEdge == 0 || targetCardEdge == 1) {
            cardToPlaceEdge = targetCardEdge + 2;
        } else {
            cardToPlaceEdge = targetCardEdge - 2;
        }

        // check myCard has an actual availableEdge
        if (!cardToPlace.edgeAvailable(cardToPlaceEdge)) {
            throw new EdgeNotFree(cardToPlace, cardToPlaceEdge);
        }


        // check targetCard has an actual availableEdge
        PlayableCard targetCard = this.boardMap.get(coordinates).getCardPointer();

        if (!targetCard.edgeAvailable(targetCardEdge)) {
            throw new EdgeNotFree(targetCard, targetCardEdge);
        }


        // check spaceAround is free

        // Define the relative coordinates to check (top-right, top-left, bottom-right, bottom-left)
        int[][] relativeCoordinates = {{1, 1}, {-1, -1}, {1, -1}, {-1, 1}};

        for (int[] offset : relativeCoordinates) {
            // Calculate the new coordinate to check
            Coordinates coordinateToCheck = new Coordinates(coordinates.getX() + offset[0], coordinates.getY() + offset[1]);

            // Check if the coordinate exists in the boardMap
            if (this.boardMap.containsKey(coordinateToCheck)) {
                PlayableCard spaceToCheck = this.boardMap.get(coordinateToCheck).getCardPointer();
                // Check if the edge is not free
                if (!spaceToCheck.edgeAvailable(targetCardEdge)) {
                    throw new EdgeNotFree(targetCard, targetCardEdge);
                }
            }
        }
    }

    public void addCardToBoard(Coordinates xy, PlayableCard cardToPlace) {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed());
        boardMap.put(xy, newCell);
    }

    // simplified for cycle to update reigns and objects
    public void resourceUpdate(PlayableCard cardToPlace, boolean isFlipped) {
        if (!isFlipped) {
            if (cardToPlace.getCardType().equals(CardType.GOLD)) {
                for (int element : cardToPlace.resourceNeeded) {
                    reignsCollected[element] -= cardToPlace.resourceNeeded[element]; // sub only for gold card
                }
            }

            // add edgeReigns played to the board
            for (boolean value : cardToPlace.getLinkableEdge()) {
                if (value) {
                    for (ReignType element : cardToPlace.reignPointEdge) {
                        int position = element.ordinal();
                        updateReignsCollected(position);
                    }
                }
            }

            for (int element : cardToPlace.resourceNeeded) {
                reignsCollected[element] += cardToPlace.resourceNeeded[element];
            }
        }
    }
}
