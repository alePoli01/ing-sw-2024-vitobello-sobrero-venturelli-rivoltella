package it.polimi.GC13.view.GUI.game;

import javax.swing.*;
import java.awt.*;

public class WinningFrame extends JFrame {
    private boolean win;
    private JLabel winnerLabel;
    private JLabel scoreLabel;

    public WinningFrame() {
        JPanel winnerPanel = new JPanel(new GridBagLayout());

        winnerLabel = createTextLabelFont("", 256);
        winnerPanel.add(winnerPanel, createGridBagConstraints(0,0));

        if(win){
            winnerLabel.setForeground(new Color(61, 168, 52));
        } else {
            winnerLabel.setForeground(new Color(205, 52, 17));
        }

        scoreLabel = createTextLabelFont("", 64);
        winnerPanel.add(scoreLabel, createGridBagConstraints(0,1));

        getContentPane().add(winnerPanel, BorderLayout.CENTER);

    }

    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    public JLabel getWinnerLabel() {
        return winnerLabel;
    }

    public void setWin(boolean win) {
        this.win = win;
    }

    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }
}
