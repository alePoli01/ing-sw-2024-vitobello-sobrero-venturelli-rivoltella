package it.polimi.GC13.view.GUI.game;

import junit.framework.TestCase;
import javax.swing.*;

public class BoardViewGUITest extends TestCase {

    public void testInsertCard() {
    }

    public void testCardPrinterGUI() {

        SwingUtilities.invokeLater(new Runnable() {
            public void run() {
                System.out.println("Created GUI on EDT? " +
                        SwingUtilities.isEventDispatchThread());
                JFrame f = new JFrame("Swing Paint Demo");
                f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                f.setSize(450, 450);
                f.setVisible(true);
            }
        });





    }
}