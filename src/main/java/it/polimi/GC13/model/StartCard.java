package it.polimi.GC13.model;

import it.polimi.GC13.enums.*;

import java.util.Map;
import java.util.ArrayList;

public class StartCard extends PlayableCard {
    public final ArrayList<Resource> frontReigns;
    public final Resource[] reignBackPointEdge;


    public StartCard(int serialNumber, Resource reign, CardType cardType, Resource[] edgeResource, Map<Resource, Integer> resourceNeeded, int pointsGiven, PointsCondition condition, ArrayList<Resource> frontReigns, Resource[] reignBackPointEdge) {
        super(serialNumber, reign, cardType, edgeResource, resourceNeeded, pointsGiven, condition);
        this.frontReigns = frontReigns;
        this.reignBackPointEdge = reignBackPointEdge;
    }
}
