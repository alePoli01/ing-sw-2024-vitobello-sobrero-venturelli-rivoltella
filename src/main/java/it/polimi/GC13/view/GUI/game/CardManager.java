package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.model.PlayableCard;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;

public interface CardManager {
    void showCard(Integer hand) throws IOException; //permette di associare alla carta con tale serialNumber i corrispettivi front e back

}
