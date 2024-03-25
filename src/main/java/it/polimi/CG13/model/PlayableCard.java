package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.PointsCondition;
import it.polimi.CG13.enums.ReignType;

import java.util.Map;

public class PlayableCard {
    private final int serialNumber;
    private final ReignType reign;
    private final CardType cardType;
    private final ReignType[] reignPointEdge;
    private final ObjectType[] objectPointEdge;
    private final Map<ReignType, Integer> resourceNeeded;
    private final int pointsGiven;
    private final PointsCondition condition;
    private boolean[] linkableEdge;
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

    // added to print the card has an error
    public int getSerialNumber() {
        return serialNumber;
    }

    public CardType getCardType() {
        return cardType;
    }

    public ObjectType[] getObjectPointEdge() {
        return objectPointEdge;
    }

    public ReignType[] getReignPointEdge() {
        return reignPointEdge;
    }

    public ObjectType getObjectPointEdge(int position) {
        return objectPointEdge[position];
    }

    public ReignType getReignPointEdge(int position) {
        return reignPointEdge[position];
    }

    public ReignType getReign() {
        return reign;
    }

    public boolean edgeAvailable(int edge){
        return linkableEdge[edge];
    }

    public void setLinkableEdge(boolean linkableEdge, int edge) {
        this.linkableEdge[edge] = linkableEdge;
    }

    public boolean[] getLinkableEdge() {
        return linkableEdge;
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

    public int getPointsGiven() {
            return pointsGiven;
    }

    public int getResourceNeeded(ReignType reign) {
        return resourceNeeded.get(reign);
    }
}
