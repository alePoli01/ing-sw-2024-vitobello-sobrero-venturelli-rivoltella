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
    private int playerScore = 0;
    private final EnumMap<Resource, Integer> collectedResources = new EnumMap<>(Resource.class);     //counter for each type of object present on the board
    private final Set<Coordinates> availableCells = new HashSet<>();
    private final Set<Coordinates> notAvailableCells = new HashSet<>();   // -> used to not add available cell
    private final List<Coordinates> offset = new LinkedList<>();

    //initialize all the values to zero
    public Board(Player owner) {
        this.owner = owner;
        // populate map with 0 for each reign and object
        for (Resource resource : Resource.values()) {
            if (resource.isObject() || resource.isReign()) {
                collectedResources.put(resource, 0);
            }
        }
        // offset to use in methods
        this.offset.add(new Coordinates(-1, +1));
        this.offset.add(new Coordinates(+1, +1));
        this.offset.add(new Coordinates(+1, -1));
        this.offset.add(new Coordinates(-1, -1));
    }

    public Map<Coordinates, Cell> getBoardMap() {
        return boardMap;
    }


    public int getPlayerScore() {
        return this.playerScore;
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
            this.owner.getGame().getObserver().notifyClients(new OnForbiddenCellMessage(owner.getNickname(), X, Y, availableCells));
            throw new GenericException("Forbidden cell " + X + ", " + Y);
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
            this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, 50, 50, owner.getTurnPlayed()));
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
        this.updateAvailableCells(cardToPlace, xy);
        this.owner.getGame().getObserver().notifyClients(new OnPlaceCardMessage(this.owner.getNickname(), cardToPlace.serialNumber, isFlipped, xy.getX(), xy.getY(), owner.getTurnPlayed()));
    }

    // updates notAvailableCells and availableCells sets
    private void updateAvailableCells(PlayableCard cardPlaced, Coordinates xy) {
        this.availableCells.remove(xy);
        this.notAvailableCells.add(xy);

        int i = 0;
        while (i < cardPlaced.edgeResource.length) {
            Resource resource = cardPlaced.edgeResource[i];
            Coordinates coordinatesToCheck = new Coordinates(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY());
            if (resource != Resource.NULL && !this.notAvailableCells.contains(getCoordinateFromBoardMap(xy.getX() + offset.get(i).getX(), xy.getY() + offset.get(i).getY()))) {
                this.availableCells.add(coordinatesToCheck);
                System.out.println("New available coordinates: (" + coordinatesToCheck.getX() + ", " + coordinatesToCheck.getY() + ")");
            } else {
                notAvailableCells.add(coordinatesToCheck);
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
                    if (!(this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[edge.getAndIncrement()].isNullOrEmpty())) {
                        for (Resource resource : this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource) {
                            if (this.boardMap.get(coordinateToCheck).getCardPointer().edgeResource[edge.getAndIncrement()].equals(resource)) {
                                if (resource.isReign() || resource.isObject()) {
                                    collectedResources.put(resource, collectedResources.get(resource) - 1);
                                }
                            }
                        }
                    }
                }
            });
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

    public boolean boardMapContainsKeyOfValue(int x, int y) {
        for(Coordinates xy : this.boardMap.keySet()){
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

    // collection to be used for set and lists
    public boolean contains(Collection<Coordinates> collection, int x, int y) {
        return collection
                .stream()
                .anyMatch(coordinates -> coordinates.getX() == x && coordinates.getY() == y);
    }

}
