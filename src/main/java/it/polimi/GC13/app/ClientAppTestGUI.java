package it.polimi.GC13.app;

import it.polimi.GC13.view.GUI.game.MainFrameProva;

import javax.swing.*;
import java.io.IOException;

public class ClientAppTestGUI {

    public static void main(String[] args) throws IOException {
        SwingUtilities.invokeLater(MainFrameProva::new);

    }
}
