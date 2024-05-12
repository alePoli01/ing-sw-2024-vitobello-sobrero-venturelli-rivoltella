package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.socket.messages.fromserver.OnPlaceCardMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnForbiddenCellMessage;
import it.polimi.GC13.network.socket.messages.fromserver.exceptions.OnNotEnoughResourceToPlaceMessage;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class Board implements Serializable {
    private final Map<Coordinates, Cell> boardMap = new HashMap<>();
    private final Player owner;               //owner of the board
    private final EnumMap<Resource, Integer> collectedResources = new EnumMap<>(Resource.class);     //counter for each type of object present on the board
    private final List<Coordinates> availableCells = new LinkedList<>();
    private final List<Coordinates> notAvailableCells = new LinkedList<>();   // -> used to not add available cell
    private final List<Coordinates> offset = new LinkedList<>();

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        // populate map with 0 for each reign and object
        Arrays.stream(Resource.values()).sequential()
                .filter(resource -> resource.isObject() || resource.isReign())
                .forEach(resource -> collectedResources.put(resource, 0));
        // offset to use in methods
        this.offset.add(new Coordinates(-1, +1));
        this.offset.add(new Coordinates(+1, +1));
        this.offset.add(new Coordinates(+1, -1));
        this.offset.add(new Coordinates(-1, -1));
    }

    public Map<Coordinates, Cell> getBoardMap() {
        return boardMap;
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

    /**
     *
     * @param X coordinate x
     * @param Y coordinate y
     * @return returns the coordinate from the board if it is possible to place or throw a GenericException
     * @throws GenericException if the cell is not available and notifying the client as well
     */
    public Coordinates isPossibleToPlace(int X, int Y) throws GenericException {
        return this.availableCells
                .stream()
                .filter(coordinates -> coordinates.getX() == X && coordinates.getY() == Y)
                .findFirst()
                .orElseThrow(() -> {
                    this.owner.getGame().getObserver().notifyClients(new OnForbiddenCellMessage(owner.getNickname(), X, Y, this.availableCells));
                    this.availableCells.forEach(cell -> System.out.println("(" + cell.getX() + ", " + cell.getY() + ") "));
                    return new GenericException("Forbidden cell " + X + ", " + Y);
                });
    }

    /** place start card to the board
     *
     * @param cardToPlace card to place on the board
     * @param isFlipped side of the card to place: true for back side and false for front side
     * @throws GenericException if the card isn't added correctly it throws an exception
     */
    public void placeStartCardOnTheBoard(PlayableCard cardToPlace, boolean isFlipped) throws GenericException {
        Coordinates xy = new Coordinates(50, 50);
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new GenericException("Sever didn't update the Board");
        } else {
            this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, 50, 50, owner.getTurnPlayed()));
        }
        this.updateAvailableCells(cardToPlace, xy, isFlipped);
    }

    /** place the card to the board
     *
     * @param xy
     * @param cardToPlace
     * @param isFlipped
     * @throws GenericException
     */
    public void placeCardToTheBoard(Coordinates xy, PlayableCard cardToPlace, boolean isFlipped) throws GenericException {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new GenericException("Sever didn't update the Board");
        }
        this.updateAvailableCells(cardToPlace, xy, isFlipped);
        this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, xy.getX(), xy.getY(), owner.getTurnPlayed()));
    }

    // updates notAvailableCells and availableCells sets
    private void updateAvailableCells(PlayableCard cardPlaced, Coordinates xy, boolean isFlipped) {
        this.availableCells.remove(xy);
        this.notAvailableCells.add(xy);

        int i = 0;
        while (i < cardPlaced.edgeResource.length) {
            Resource resource = cardPlaced.edgeResource[i];
            Coordinates coordinatesToCheck = new Coordinates(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY());

            // if the coordinates has X on the edge, and it is not already forbidden it is added to the forbidden
            if (isFlipped) {
                this.availableCells.add(coordinatesToCheck);
            } else if (resource.equals(Resource.NULL) && !this.notAvailableCells.contains(getCoordinateFromBoardMap(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY()))) {
                notAvailableCells.add(coordinatesToCheck);
                // else it is added to availableCells
            } else {
                this.availableCells.add(coordinatesToCheck);
            }
            i++;
        }
    }

    // method used to cycle on surrounding coordinate (atm used only to count gold card given points)
    public int surroundingCardsNumber(int x, int y) {
        AtomicInteger counter = new AtomicInteger(0);

        this.offset
            .forEach(offset -> {
                Coordinates coordinateToCheck = this.getCoordinateFromBoardMap(x + offset.getX(), y + offset.getY());
                if (this.boardMap.containsKey(coordinateToCheck)) {
                    counter.incrementAndGet();
                }
            });
        return counter.get();
    }

    // update surrounding cards edges
    public void removeResources(int x, int y) {
        AtomicInteger edge = new AtomicInteger(0);

        this.offset
            .forEach(offset -> {
                Coordinates coordinateToCheck = this.getCoordinateFromBoardMap(x + offset.getX(), y + offset.getY());
                if (this.boardMap.containsKey(coordinateToCheck) && ((!this.boardMap.get(coordinateToCheck).isFlipped) || this.boardMap.get(coordinateToCheck).getCardPointer().cardType.equals(CardType.STARTER))) {
                    // determine if a new covered edge has reign or object and in case remove it from availableResources
                    if (!(this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[edge.get()].isNullOrEmpty())) {
                        for (Resource resource : this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource) {
                            if (this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[edge.getAndIncrement()].equals(resource)) {
                                if (resource.isReign() || resource.isObject()) {
                                    collectedResources.put(resource, collectedResources.get(resource) - 1);
                                }
                            }
                        }
                        edge.incrementAndGet();
                    }
                }
            });
    }

    // simplified for cycles to update reigns and objects
    public void addResource(PlayableCard cardToPlace, boolean isFlipped) {
        // if statement for start card, else for gold / resources
        if (cardToPlace.serialNumber >= 81 && cardToPlace.serialNumber <= 86) {
            if (isFlipped) {
                Arrays.stream(((StartCard) cardToPlace).reignBackPointEdge)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            } else {
                Arrays.stream(((StartCard) cardToPlace).frontReigns)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            }
        } else if (isFlipped) {
            collectedResources.put(cardToPlace.reign, collectedResources.get(cardToPlace.reign) + 1);
        }

        if (!isFlipped) {
            // add card played resource to the board
            Arrays.stream(cardToPlace.edgeResource)
                    .filter(resource -> resource.isObject() || resource.isReign())
                    .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
        }
    }

    public boolean boardMapContainsKeyOfValue(int x, int y) {
        for(Coordinates xy : this.boardMap.keySet()) {
            if (xy.getX() == x && xy.getY() == y){
                return true;
            }
        }
        return false;
    }

    public Coordinates getCoordinateFromBoardMap(int x, int y) {
        for (Coordinates xy : this.boardMap.keySet()) {
            if (xy.getX() == x && xy.getY() == y){
                return xy;
            }
        }
        return null;
    }
}
