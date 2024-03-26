package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.PointsCondition;
import it.polimi.CG13.enums.ReignType;

import java.util.Map;

public class PlayableCard {
    public final int serialNumber;
    public final ReignType reign;
    public final CardType cardType;
    public final ReignType[] reignPointEdge;
    public final ObjectType[] objectPointEdge;
    public final Map<ReignType, Integer> resourceNeeded;
    public final int pointsGiven;
    public final PointsCondition condition;
    public final boolean[] linkableEdge;
    private PlayableCard[] linkedCard;

    public PlayableCard(int serialNumber, boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectPointEdge, Map<ReignType, Integer> resourceNeeded, int pointsGiven, PointsCondition condition) {
        this.serialNumber = serialNumber;
        this.reign = reign;
        this.cardType = cardType;
        this.linkableEdge = linkableEdge;
        this.reignPointEdge = reignPointEdge;
        this.objectPointEdge = objectPointEdge;
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
