package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;

public class PlayableCard {
    public final int serialNumber;
    public final EdgeResources reign;
    public final CardType cardType;
    public final EdgeResources[] edgeResource;
    public final Map<EdgeResources, Integer> resourceNeeded;
    public final int pointsGiven;
    public final PointsCondition condition;
    private PlayableCard[] linkedCard;

    public PlayableCard(int serialNumber, EdgeResources reign, CardType cardType, EdgeResources[] edgeResource, Map<EdgeResources, Integer> resourceNeeded, int pointsGiven, PointsCondition condition) {
        this.serialNumber = serialNumber;
        this.reign = reign;
        this.cardType = cardType;
        this.edgeResource = edgeResource;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
        this.condition = condition;
    }

    // method to calculate points given after a gold card is placed
    public int getPointsGiven(Board board, Coordinates xy) {
        if (this.condition != null) {
            return switch (condition) {
                case QUILL -> board.getCollectedResources().get(EdgeResources.QUILL) * pointsGiven;
                case MANUSCRIPT -> board.getCollectedResources().get(EdgeResources.MANUSCRIPT) * pointsGiven;
                case INKWELL -> board.getCollectedResources().get(EdgeResources.INKWELL) * pointsGiven;
                case EDGE -> board.surroundingCardsNumber(xy) * pointsGiven;
            };
        } else {
            return pointsGiven;
        }
    }
}
