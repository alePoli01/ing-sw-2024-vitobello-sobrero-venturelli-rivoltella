package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;
import it.polimi.GC13.exception.GenericException;
import it.polimi.GC13.network.messages.fromserver.OnPlaceCardMessage;
import it.polimi.GC13.network.messages.fromserver.OnUpdateResourceMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnForbiddenCellMessage;
import it.polimi.GC13.network.messages.fromserver.exceptions.OnNotEnoughResourceToPlaceMessage;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * {@code Board} class represents the game board. Each player has a board, in which players can place their cards during the game
 * and all the collected resources are stored. <br>
 * The class implements {@link Serializable} to support object serialization.
 */
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
     *Constructs a new {@code Board} for the specified owner player.
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

    /**
     * Retrieves the map representing the board layout.
     *
     * @return The map of coordinates to cells on the board.
     */
    public Map<Coordinates, Cell> getBoardMap() {
        return boardMap;
    }

    /**
     * Retrieves the collected resources on the board.
     *
     * @return The EnumMap containing counts of each resource type collected on the board.
     */
    public EnumMap<Resource, Integer> getCollectedResources() {
        return this.collectedResources;
    }

    /**
     * Verifies if the resources required by the given card can be placed on the board.
     *
     * @param cardToPlace The card to be placed on the board.
     * @throws GenericException If there are not enough resources on the board to place the card.
     */
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
     * Checks if it's possible to place a card at the specified coordinates on the board.
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
                    return new GenericException("Cell " + X + ", " + Y + " not available");
                });
    }

    /**
     * Places the specified card on the board at the given coordinates.
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

    /**
     * Updates the available and not available cells on the board after placing a card.
     *
     * @param cardPlaced   The card that was placed on the board.
     * @param xy           The coordinates where the card was placed.
     * @param isFlipped    Whether the card is flipped or not.
     * @param edgeResource The resources on the edge of the card.
     * @return The updated list of available cells.
     */
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

    /**
     * Counts the number of surrounding cards at the specified coordinates.
     *
     * @param x The x-coordinate to check.
     * @param y The y-coordinate to check.
     * @return The number of surrounding cards.
     */
    // method used to cycle on surrounding coordinate (atm used only to count gold card given points)
    public int surroundingCardsNumber(int x, int y) {
        AtomicInteger counter = new AtomicInteger(0);

        this.offset
            .forEach(offset -> {
                if (checkListContainsCoordinates(this.boardMap.keySet(), new Coordinates(x + offset.getX(), y + offset.getY()))) {
                    counter.incrementAndGet();
                }
            });
        return counter.get();
    }

    /**
     * Removes resources from the board when a card covers them.
     *
     * @param x The x-coordinate to remove resources.
     * @param y The y-coordinate to remove resources.
     */
    // update surrounding cards edges
    public void removeResources(int x, int y) {
        AtomicInteger edge = new AtomicInteger(2);

        this.offset
            .forEach(offset -> {
                if (checkListContainsCoordinates(this.boardMap.keySet(), new Coordinates(x + offset.getX(), y + offset.getY()))) {
                    Coordinates coordinateToCheck = getCoordinateFromList(this.boardMap.keySet(), new Coordinates(x + offset.getX(), y + offset.getY()));
                    // if the covered edge is of a starter card
                    if (this.boardMap.get(coordinateToCheck).getCardPointer().cardType.equals(CardType.STARTER)) {
                        StartCard cardToRemoveResource = (StartCard) this.boardMap.get(coordinateToCheck).getCardPointer();
                        this.collectedResources.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(this.boardMap.get(coordinateToCheck).isFlipped ? cardToRemoveResource.edgeBackResource[edge.get()] : cardToRemoveResource.edgeFrontResource[edge.get()]))
                                .forEach(entry -> entry.setValue(entry.getValue() - 1));
                    } else if (!this.boardMap.get(coordinateToCheck).isFlipped) {
                        PlayableCard cardToRemoveResource = this.boardMap.get(coordinateToCheck).getCardPointer();
                        // determine if a new covered edge has reign or object and in case remove it from availableResources
                        this.collectedResources.entrySet().stream()
                                .filter(entry -> entry.getKey().equals(cardToRemoveResource.edgeFrontResource[edge.get()]))
                                .forEach(entry -> entry.setValue(entry.getValue() - 1));
                    }
                }
                edge.incrementAndGet();
                // reset the edge to check -> edge covered are different from the card placed that covers that edge. The order is 2 -> 3 -> 0 -> 1
                edge.compareAndSet(4, 0);
            });
    }

    /**
     * Adds resources to the board based on the card placed and its orientation.
     *
     * @param cardToPlace The card that is being placed on the board.
     * @param isFlipped   Indicates whether the card is flipped (back side) or not.
     */
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
        System.out.println( collectedResources.entrySet().stream().toList()+"\n");
        this.owner.getGame().getObserver().notifyClients(new OnUpdateResourceMessage(this.owner.getNickname(), new EnumMap<>(this.collectedResources)));
    }

    /**
     * Checks if a given set of coordinates exists within a list of coordinates.
     *
     * @param coordinatesList   The list of coordinates to check.
     * @param coordinatesToCheck The coordinates to search for in the list.
     * @return True if the coordinates list contains the coordinates to check, false otherwise.
     */
    public boolean checkListContainsCoordinates(Set<Coordinates> coordinatesList, Coordinates coordinatesToCheck) {
        return coordinatesList.stream()
                .anyMatch(coordinate -> coordinate.equals(coordinatesToCheck));
    }

    /**
     * Retrieves a specific coordinate from a list of coordinates, if it exists.
     *
     * @param coordinatesList   The list of coordinates to search within.
     * @param coordinatesToCheck The coordinates to retrieve from the list.
     * @return The found coordinates if present in the list, otherwise null.
     */
    public Coordinates getCoordinateFromList(Set<Coordinates> coordinatesList, Coordinates coordinatesToCheck) {
        return coordinatesList.stream()
                .filter(coordinate -> coordinate.equals(coordinatesToCheck))
                .findFirst()
                .orElse(null);
    }
}
