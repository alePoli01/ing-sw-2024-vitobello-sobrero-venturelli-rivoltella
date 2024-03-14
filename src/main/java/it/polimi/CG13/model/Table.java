package it.polimi.CG13.model;

public class Table {
    private int[] score;
    private PlayableCard[] resourceFacedUp;
    private PlayableCard[] goldFacedUp;
    private PlayableCard resourceFacedDown;
    private PlayableCard goldFacedDown;

    public void getPlayerTurn() {}

    public PlayableCard getNewCard(Player player, PlayableCard card) {

        return card;
    }
}
