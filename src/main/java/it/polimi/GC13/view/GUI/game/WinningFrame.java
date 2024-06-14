package it.polimi.GC13.view.GUI.game;

import javax.swing.*;
import java.awt.*;

public class WinningFrame extends JFrame {
    private JLabel winnerLabel;
    private JLabel scoreLabel;

    public WinningFrame() {
        setTitle("Winning Frame");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(true);

        JPanel winnerPanel = new JPanel(new GridBagLayout());

        winnerLabel = createTextLabelFont("", 256);
        winnerPanel.add(winnerLabel, createGridBagConstraints(0,0));

        scoreLabel = createTextLabelFont("", 64);
        winnerPanel.add(scoreLabel, createGridBagConstraints(0,1));

        getContentPane().add(winnerPanel, BorderLayout.CENTER);

        setVisible(true);
    }

    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    public JLabel getWinnerLabel() {
        return winnerLabel;
    }

    public JLabel getScoreLabel() {
        return scoreLabel;
    }

    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }
}
