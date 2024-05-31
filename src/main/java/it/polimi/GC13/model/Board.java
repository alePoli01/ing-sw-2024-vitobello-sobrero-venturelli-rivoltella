package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.messages.fromserver.OnPlaceCardMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnForbiddenCellMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnNotEnoughResourceToPlaceMessage;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collectors;

public class Board implements Serializable {
    private final Map<Coordinates, Cell> boardMap = new HashMap<>();
    private final Player owner;               //owner of the board
    private final EnumMap<Resource, Integer> collectedResources = new EnumMap<>(Resource.class);     //counter for each type of object present on the board
    private final List<Coordinates> availableCells = new LinkedList<>();
    private final List<Coordinates> notAvailableCells = new LinkedList<>();   // -> used to not add available cell
    private final List<Coordinates> offset = List.of(
            new Coordinates(-1, +1),
            new Coordinates(+1, +1),
            new Coordinates(+1, -1),
            new Coordinates(-1, -1)
    );

    /**
     *
     * @param owner player that will place his cards on this board
     */
    public Board(Player owner) {
        this.owner = owner;
        // populate map with 0 for each reign and object
        Arrays.stream(Resource.values()).sequential()
                .filter(resource -> resource.isObject() || resource.isReign())
                .forEach(resource -> collectedResources.put(resource, 0));
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

    /** place the card to the board
     *
     * @param xy coordinate to place card
     * @param cardToPlace card to place on the board
     * @param isFlipped side to place the card
     * @throws GenericException exception thrown in case the card isn't placed on the board for any reason
     */
    public void placeCardToTheBoard(Coordinates xy, PlayableCard cardToPlace, boolean isFlipped) throws GenericException {
        Cell newCell = new Cell(cardToPlace, owner.getTurnPlayed(), isFlipped);
        boardMap.put(xy, newCell);
        if (!boardMap.get(xy).getCardPointer().equals(cardToPlace)) {
            throw new GenericException("Sever didn't update the Board");
        }
        if (cardToPlace.cardType.equals(CardType.STARTER)) {
            this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, xy.getX(), xy.getY(), owner.getTurnPlayed(), this.updateAvailableCells(cardToPlace, xy, isFlipped, isFlipped ? ((StartCard) cardToPlace).edgeBackResource : cardToPlace.edgeFrontResource)));
        } else {
            this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, xy.getX(), xy.getY(), owner.getTurnPlayed(), this.updateAvailableCells(cardToPlace, xy, isFlipped, cardToPlace.edgeFrontResource)));
        }
    }

    // updates notAvailableCells and availableCells sets
    private List<Coordinates> updateAvailableCells(PlayableCard cardPlaced, Coordinates xy, boolean isFlipped, Resource[] edgeResource) {
        this.availableCells.remove(xy);
        this.notAvailableCells.add(xy);

        int i = 0;
        // if the card is gold / resource and is placed on back it's not important to check the edges
        // same things for starter card on front
        if (cardPlaced.serialNumber < 81 && isFlipped || (cardPlaced.cardType.equals(CardType.STARTER) && !isFlipped)) {
            this.offset
                    .forEach(offset -> {
                        Coordinates coordinatesToCheck = new Coordinates(xy.getX() + offset.getX(), xy.getY() + offset.getY());
                        if (!checkListContainsCoordinates(new HashSet<>(this.notAvailableCells), coordinatesToCheck)) {
                            this.availableCells.add(coordinatesToCheck);
                        }
                    });
        } else {
            while (i < edgeResource.length) {
                Coordinates coordinatesToCheck = new Coordinates(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY());

                // check if the coordinates are in the notAvailableCells or not
                if (!checkListContainsCoordinates(new HashSet<>(this.notAvailableCells), coordinatesToCheck)) {
                    // if the card has "NULL" on the edge and the coordinates are not already forbidden, these coordinates are added to the forbidden
                    if (edgeResource[i].equals(Resource.NULL)) {
                        this.notAvailableCells.add(coordinatesToCheck);
                        // if the coordinates are in the availableCells, they are removed
                        if (checkListContainsCoordinates(new HashSet<>(this.availableCells), coordinatesToCheck)) {
                            this.availableCells.remove(getCoordinateFromList(new HashSet<>(this.availableCells), coordinatesToCheck));
                        }
                    } else {
                        // else they are added to the availableCells
                        this.availableCells.add(coordinatesToCheck);
                    }
                }
                i++;
            }
        }
        return new LinkedList<>(this.availableCells);
    }

    // method used to cycle on surrounding coordinate (atm used only to count gold card given points)
    public int surroundingCardsNumber(int x, int y) {
        AtomicInteger counter = new AtomicInteger(0);

        this.offset
            .forEach(offset -> {
                if (checkListContainsCoordinates(this.boardMap.keySet(), new Coordinates(x, y))) {
                    counter.incrementAndGet();
                }
            });
        return counter.get();
    }

    // update surrounding cards edges
    public void removeResources(int x, int y, Resource[] resourcesToCheck) {
        AtomicInteger edge = new AtomicInteger(0);

        this.offset
            .forEach(offset -> {
                if (checkListContainsCoordinates(this.boardMap.keySet(), new Coordinates(x + offset.getX(), y + offset.getY()))) {
                    Coordinates coordinateToCheck = getCoordinateFromList(this.boardMap.keySet(), new Coordinates(x + offset.getX(), y + offset.getY()));
                    // if the covered edge is of a card that is on the back side and it is not a starter card
                    if ((!this.boardMap.get(coordinateToCheck).isFlipped && !this.boardMap.get(coordinateToCheck).getCardPointer().cardType.equals(CardType.STARTER)) || this.boardMap.get(coordinateToCheck).getCardPointer().cardType.equals(CardType.STARTER)) {
                        // determine if a new covered edge has reign or object and in case remove it from availableResources
                        this.collectedResources.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(resourcesToCheck[edge.get()]))
                                .forEach(entry -> entry.setValue(entry.getValue() - 1));
                    }
                }
                edge.incrementAndGet();
            });
    }

    // simplified for cycles to update reigns and objects
    public void addResource(PlayableCard cardToPlace, boolean isFlipped) {
        // if card is on the back side
        if (isFlipped) {
            if (cardToPlace.cardType.equals(CardType.STARTER)) {
                Arrays.stream(((StartCard) cardToPlace).edgeBackResource)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));

                Arrays.stream(((StartCard) cardToPlace).backReigns)
                        .filter(Resource::isReign)
                        .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
            } else {
                // if the card isn't starter, it will be added the played card reign to the board
                collectedResources.put(cardToPlace.reign, collectedResources.get(cardToPlace.reign) + 1);
            }
        } else {
            Arrays.stream(cardToPlace.edgeFrontResource)
                    .filter(resource -> resource.isObject() || resource.isReign())
                    .forEach(resource -> collectedResources.put(resource, collectedResources.get(resource) + 1));
        }
    }

    public boolean checkListContainsCoordinates(Set<Coordinates> coordinatesList, Coordinates coordinatesToCheck) {
        return coordinatesList.stream()
                .anyMatch(coordinate -> coordinate.equals(coordinatesToCheck));
    }

    public Coordinates getCoordinateFromList(Set<Coordinates> coordinatesList, Coordinates coordinatesToCheck) {
        return coordinatesList.stream()
                .filter(coordinate -> coordinate.equals(coordinatesToCheck))
                .findFirst()
                .orElse(null);
    }
}
