package it.polimi.CG13.model;

import it.polimi.CG13.enums.CardType;
import it.polimi.CG13.enums.ObjectType;
import it.polimi.CG13.enums.ReignType;

public class PlayableCard {
    final int serialNumber;
    final ReignType reign;
    final CardType cardType;
    final ReignType[] reignPointEdge;
    final ObjectType[] objectType;
    final int[] resourceNeeded;
    final int pointsGiven;
    private boolean[] linkableEdge;

    public PlayableCard(int serialNumber, boolean[] linkableEdge, ReignType reign, CardType cardType, ReignType[] reignPointEdge, ObjectType[] objectType, int[] resourceNeeded, int pointsGiven) {
        this.serialNumber = serialNumber;
        this.linkableEdge = linkableEdge;
        this.reign = reign;
        this.cardType = cardType;
        this.reignPointEdge = reignPointEdge;
        this.objectType = objectType;
        this.resourceNeeded = resourceNeeded;
        this.pointsGiven = pointsGiven;
    }

    // added to print the card has an error
    public int getSerialNumber() {
        return serialNumber;
    }

    public CardType getCardType() {
        return cardType;
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

    public int getPointsGiven() {
        return pointsGiven;
    }

    public int getResourceNeeded(int position) {
        return resourceNeeded[position];
    }
}
