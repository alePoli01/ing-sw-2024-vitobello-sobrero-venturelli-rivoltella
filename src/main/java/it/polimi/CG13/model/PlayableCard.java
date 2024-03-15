package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;

public class PlayableCard {

    private boolean[] linkableEdge;
    private ReignType reign;
    private CardType cardType;
    private ReignType[] reignPointEdge;
    private ObjectType[] objectType;
    private int[] resourceNeeded;
    private int pointsGiven;

    public PlayableCard(boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectType, int[] resourceNeeded, int pointsGiven) {
        this.linkableEdge = linkableEdge;
        this.reign = reign;
        this.cardType = cardType;
        this.reignPointEdge = reignPointEdge;
        this.objectType = objectType;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
    }
}
