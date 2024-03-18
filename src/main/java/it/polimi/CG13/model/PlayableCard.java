package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;

public class PlayableCard {
    final ReignType reign;
    final CardType cardType;
    final ReignType[] reignPointEdge;
    final ObjectType[] objectType;
    final int[] resourceNeeded;
    final int pointsGiven;
    private boolean[] linkableEdge;

    public PlayableCard(boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectType, int[] resourceNeeded, int pointsGiven) {
        this.linkableEdge = linkableEdge;
        this.reign = reign;
        this.cardType = cardType;
        this.reignPointEdge = reignPointEdge;
        this.objectType = objectType;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
    }

    public CardType getCardType() {
        return cardType;
    }

    public boolean edgeAvailable(int edge){
        return linkableEdge[edge];
    }

    public int getPointsGiven() {
        return pointsGiven;
    }

    public int getResourceNeeded(int position) {
        return resourceNeeded[position];
    }
}
