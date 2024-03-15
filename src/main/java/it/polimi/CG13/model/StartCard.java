package it.polimi.CG13.model;

import it.polimi.CG13.enums.ReignType;

public class StartCard extends PlayableCard{
    private ReignType[] frontReigns;
    private ReignType[] reignBackPointEdge;

    //constructor

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
