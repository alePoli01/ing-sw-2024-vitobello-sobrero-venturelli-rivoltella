package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;

public class StartCard extends PlayableCard{
    private ReignType[] frontReigns;
    private ReignType[] reignBackPointEdge;

    public StartCard(int serialNumber, boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectType, int[] resourceNeeded, int pointsGiven) {
        super(serialNumber, linkableEdge, reign, cardType, reignPointEdge, objectType, resourceNeeded, pointsGiven);
    }

    //constructor aggiunto seguendo intellij

    public ReignType[] getFrontReigns() {
        return frontReigns;
    }
    public void setFrontReigns(ReignType[] frontReigns) {
        this.frontReigns = frontReigns;
    }

    public ReignType[] getReignBackPointEdge() {
        return reignBackPointEdge;
    }
    public void setReignBackPointEdge(ReignType[] reignBackPointEdge) {
        this.reignBackPointEdge = reignBackPointEdge;
    }
}
