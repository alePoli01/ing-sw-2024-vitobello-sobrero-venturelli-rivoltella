package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.Player;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class Token {
    private JLabel label;
    private int x;
    private int y;
    private Player player;
    private String directoryImage;


    public Token(/*Player player,*/ String directory, JLabel label){
        //this.player = player;
        this.directoryImage = directory;
        this.label=label;
        }


    public String getDirectoryImage() {
        return directoryImage;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public JLabel getLabel() {
        return label;
    }
    /*public void setLabel(JLabel label) {
        this.label = label;
    }*/
}
