package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.Cell;
import it.polimi.GC13.model.Deck;
import it.polimi.GC13.model.PlayableCard;
import it.polimi.GC13.model.StartCard;
import javax.swing.*;

import java.awt.*;

public class BoardViewGUI {
    public Cell[][] Board;
    Deck deck;
    public int x_center,y_center;
    public String GoldCardFront = "gold_card_front_";
    public String GoldCardBack = "gold_card_back_";
    public String ResourceCardFront = "resource_card_front_";
    public String ResourceCardBack = "resource_card_back_";
    public String objectiveCardFront = "objective_card_front_";
    public String objectiveCardBack = "objective_card_back_";
    public String starterCardFront = "starter_card_front_";
    public String starterCardBack = "starter_card_back_";

    public BoardViewGUI() {
        Board = new Cell[95][95];
        x_center = 50;
        y_center = 50;
        deck= new Deck();
    }

    public void insertCard(int y, int x, int serialNumber, int z, boolean isFlipped){
        if (serialNumber >= 1 && serialNumber <= 40) {
            for (PlayableCard card : this.deck.getResourceDeck()) {
                if (serialNumber == card.serialNumber) {
                    this.Board[y][x] = new Cell(card, z, isFlipped);
                }
            }
        } else if (serialNumber > 40 && serialNumber <= 80) {
            for (PlayableCard card : this.deck.getGoldDeck()) {
                if (serialNumber == card.serialNumber) {
                    this.Board[y][x] = new Cell(card, z, isFlipped);
                }
            }
        } else if (serialNumber > 80 && serialNumber <= 86) {
            for (StartCard card : this.deck.getStartDeck()) {
                if (serialNumber == card.serialNumber) {
                    this.Board[y][x] = new Cell(card, z, isFlipped);
                }
            }
        }
    }

    public void cardPrinterGUI(int serialnumber, boolean isflipped,GraphicsEnvironment g){
        ClassLoader classLoader = getClass().getClassLoader();
        double x=100;
        double y=100;

    }
}
