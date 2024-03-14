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

}
