package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;
import java.util.ArrayList;

public class StartCard extends PlayableCard {
    public final ArrayList<ReignType> frontReigns;
    public final ReignType[] reignBackPointEdge;


    public StartCard(int serialNumber, ReignType reign, CardType cardType, Resource[] resourceEdge, Map<ReignType, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, ArrayList<ReignType> frontReigns, ReignType[] reignBackPointEdge) {
        super(serialNumber, reign, cardType, resourceEdge, resourceNeeded, pointsGiven, condition);
        this.frontReigns = frontReigns;
        this.reignBackPointEdge = reignBackPointEdge;
    }
}
