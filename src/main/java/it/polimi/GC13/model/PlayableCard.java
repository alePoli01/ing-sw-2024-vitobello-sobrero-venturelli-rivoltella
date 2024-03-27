package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;

public class PlayableCard {
    public final int serialNumber;
    public final ReignType reign;
    public final CardType cardType;
    public final Resource[] edgeResource;
    public final Map<ReignType, Integer> resourceNeeded;
    public final int pointsGiven;
    public final PointsCondition condition;
    private PlayableCard[] linkedCard;

    public PlayableCard(int serialNumber, ReignType reign, CardType cardType, Resource[] edgeResource, Map<ReignType, Integer> resourceNeeded, int pointsGiven, PointsCondition condition) {
        this.serialNumber = serialNumber;
        this.reign = reign;
        this.cardType = cardType;
        this.edgeResource = edgeResource;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
        this.condition = condition;
    }

    public int getPointsGiven(Board board, Coordinates xy) {
        if (this.condition != null) {
            return switch (condition) {
                case QUILL -> board.getObjectsCollected().get(ObjectType.QUILL) * pointsGiven;
                case MANUSCRIPT -> board.getObjectsCollected().get(ObjectType.MANUSCRIPT) * pointsGiven;
                case INKWELL -> board.getObjectsCollected().get(ObjectType.INKWELL) * pointsGiven;
                case EDGE -> board.surroundingCardsNumber(xy) * pointsGiven;
            };
        } else {
            return pointsGiven;
        }
    }
}
