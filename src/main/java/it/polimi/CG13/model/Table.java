package it.polimi.CG13.model;


//class that represent the table, common between players. Each game has one table, with card to pick and the score of each player
public class Table {

    //private int[] score={0,0,0,0}; //vector that keeps players scores
    private PlayableCard[] resourceFacedUp={null,null}; //resource cards faced up that can be picked
    private PlayableCard[] goldFacedUp={null,null}; //gold cards faced up that can be picked
    private PlayableCard resourceFacedDown; //resource card on the top of the deck
    private PlayableCard goldFacedDown;//gold card on the top of the deck

    //constructor of table
    public Table() {

    }

    /*
       DA DISCUTERE
    return score of the player of position index
    public int getScore(int index) {
        return score[index];
    }
    //set score of the player of position index
    public void setScore(int index,int score) {
        this.score[index]= score;
    }*/


    //return the resource card requested by parameter index(0 or 1)
    public PlayableCard getResourceFacedUp(int index) {
        return resourceFacedUp[index];
    }

    //set the new resource card by parameter index(0 or 1) and card(from the deck)
    public void setResourceFacedUp(int index,PlayableCard card ) {
        resourceFacedUp[index] = card;
    }


    //return the gold card faced up by parameter index(0 or 1)
    public PlayableCard getGoldFacedUp(int index) {
        return goldFacedUp[index];
    }
    //set the gold card faced up by parameter index(0 or 1) and card(from deck)
    public void setGoldFacedUp(PlayableCard card, int index) {
        goldFacedUp[index] = card;
    }


    //return the card on the top of the resource deck
    public PlayableCard getResourceFacedDown() {
        return resourceFacedDown;
    }
    //set the top card of the gold deck(after a pick)
    public void setResourceFacedDown(PlayableCard card) {
        this.resourceFacedDown = card;
    }


    //return the card on the top of the gold deck
    public PlayableCard getGoldFacedDown() {
        return goldFacedDown;
    }
    //set the card on the top of the gold deck(after a pick)
    public void setGoldFacedDown(PlayableCard goldFacedDown) {
        this.goldFacedDown = goldFacedDown;
    }


    //method to pick a card from the table after
    public PlayableCard getNewCard(Player player, PlayableCard card) {

        return card;
    }
}
