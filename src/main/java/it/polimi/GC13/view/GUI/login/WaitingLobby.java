package it.polimi.GC13.view.GUI.login;

import it.polimi.GC13.enums.TokenColor;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;

public class WaitingLobby extends JFrame{
    int progress;
    private ArrayList<Color> colors = new ArrayList<>();
    private int colorIndex = 0;
    private final JLabel label3 = new JLabel();

    public WaitingLobby(int readyPlayers, int neededPlayers) {
        super("Waiting Lobby");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(600, 500);
        setLocationRelativeTo(null);
        setResizable(false);
        setLayout(new GridBagLayout());
        getContentPane().setBackground(new Color(237,230,188,255));

        /*for(TokenColor color: tokenColorList){
            switch (color){
                case BLUE -> colors.add(new Color(107,189,192,255));
                case RED -> colors.add(new Color(233,73,23,255));
                case GREEN ->colors.add(new Color(113,192,124,255));
                case YELLOW -> colors.add(new Color(171,63,148,255));
            }
        }*/

        for(int i=0; i<4; i++) {
            colors.add(new Color(107, 189, 192, 255));
            colors.add(new Color(233, 73, 23, 255));
            colors.add(new Color(113, 192, 124, 255));
            colors.add(new Color(171, 63, 148, 255));
        }


        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;
        JLabel label = new JLabel("Welcome to waiting lobby", SwingConstants.CENTER);
        label.setFont(new Font("Old English Text MT", Font.BOLD, 35));
        Insets labelInsets = new Insets(0, 0, 70, 0); // padding: top, left, bottom, right
        label.setBorder(BorderFactory.createEmptyBorder(labelInsets.top, labelInsets.left, labelInsets.bottom, labelInsets.right));
        add(label, gbc);

        gbc.gridy = 1;
        label3.setText(readyPlayers + "/" + neededPlayers);
        JLabel label2 = new JLabel("Waiting for the players: " + label3.getText(), SwingConstants.CENTER);
        label2.setFont(new Font("Old English Text MT", Font.BOLD, 20));
        Insets label2Insets = new Insets(0, 0, 50, 0); // padding: top, left, bottom, right
        label2.setBorder(BorderFactory.createEmptyBorder(label2Insets.top, label2Insets.left, label2Insets.bottom, label2Insets.right));
        add(label2, gbc);

        gbc.gridy = 2;
        JProgressBar progressBar = new JProgressBar(0, 100);
        progressBar.setPreferredSize(new Dimension(300,30));
        progressBar.setForeground(colors.get(colorIndex));
        colorIndex++;
        add(progressBar, gbc);

        Timer timer = new Timer(100, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                progress += 5; // Increment progress
                if (progress > 100) {
                    progress = 0; // Reset progress
                    progressBar.setForeground(colors.get(colorIndex));
                    colorIndex = (colorIndex + 1) % colors.size();
                }
                progressBar.setValue(progress); // Update progress bar
            }
        });
        timer.start();

        setVisible(true);

    }

    public JLabel getJLabel() {
        return this.label3;
    }
}
