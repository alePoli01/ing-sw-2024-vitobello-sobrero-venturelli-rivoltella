package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.PointsCondition;
import it.polimi.CG13.enums.ReignType;

import java.util.Map;

public class StartCard extends PlayableCard {
    private final ReignType[] frontReigns;
    private final ReignType[] reignBackPointEdge;

    public StartCard(int serialNumber, boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectPointEdge, Map<ReignType, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, ReignType[] frontReigns, ReignType[] reignBackPointEdge) {
        super(serialNumber, linkableEdge, reign, cardType, reignPointEdge, objectPointEdge, resourceNeeded, pointsGiven, condition);
        this.frontReigns = frontReigns;
        this.reignBackPointEdge = reignBackPointEdge;
    }


    public ReignType[] getFrontReigns() {
        return frontReigns;
    }

    public ReignType[] getReignBackPointEdge() {
        return reignBackPointEdge;
    }

}
