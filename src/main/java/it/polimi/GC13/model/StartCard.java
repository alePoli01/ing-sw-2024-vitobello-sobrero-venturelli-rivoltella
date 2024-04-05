package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;
import java.util.ArrayList;

public class StartCard extends PlayableCard {
    public final ArrayList<EdgeResources> frontReigns;
    public final EdgeResources[] reignBackPointEdge;


    public StartCard(int serialNumber, EdgeResources reign, CardType cardType, EdgeResources[] edgeResource, Map<EdgeResources, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, ArrayList<EdgeResources> frontReigns, EdgeResources[] reignBackPointEdge) {
        super(serialNumber, reign, cardType, edgeResource, resourceNeeded, pointsGiven, condition);
        this.frontReigns = frontReigns;
        this.reignBackPointEdge = reignBackPointEdge;
    }
}
