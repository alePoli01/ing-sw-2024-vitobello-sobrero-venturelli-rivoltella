package it.polimi.CG13.model;

import it.polimi.CG13.enums.Color;

public class Player {
    final String nickname;
    private int turn;
    private Color token;
    private PlayableCard[] hand;
    private ObjectiveCard objectiveCard;

    public Player(String nickname, Color token, PlayableCard[] hand) {
        this.nickname = nickname;
    }

}
