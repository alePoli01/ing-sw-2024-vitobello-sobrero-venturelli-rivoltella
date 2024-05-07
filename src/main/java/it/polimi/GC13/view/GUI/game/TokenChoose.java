package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.network.ServerInterface;
import it.polimi.GC13.view.GUI.FrameManager;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.ArrayList;

public class TokenChoose extends JFrame {

    public TokenChoose(FrameManager frameManager) {
        setTitle("Codex Naturalis");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        //setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setExtendedState(JFrame.MAXIMIZED_BOTH);
        setResizable(false);


        JPanel container = new JPanel(new GridBagLayout());
        container.setBackground(new Color(237,230,188,255));
        add(container, BorderLayout.CENTER);
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = 0;
        gbc.gridy = 0;

        JLabel label = new JLabel("Choose your token: ", SwingConstants.CENTER);
        label.setFont(new Font("Old English Text MT", Font.BOLD, 64));
        Insets labelInsets = new Insets(0, 0, 90, 0); // padding: top, left, bottom, right
        label.setBorder(BorderFactory.createEmptyBorder(labelInsets.top, labelInsets.left, labelInsets.bottom, labelInsets.right));
        container.add(label, gbc);

        gbc.gridy = 1;
        JPanel tokenPanel = new JPanel(new FlowLayout());
        tokenPanel.setOpaque(false);
        container.add(tokenPanel, gbc);

        File directory = new File("src/main/utils/token/playableToken");
        ArrayList<JLabel> labels = new ArrayList<>();
        ArrayList<String> tokenNames = new ArrayList<>();
        ArrayList<JCheckBox> checkBoxes = new ArrayList<>();

        if (directory.exists() && directory.isDirectory()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    JLabel l = new JLabel(new ImageIcon(new ImageIcon(directory+"/"+file.getName()).getImage().getScaledInstance(300, 300, Image.SCALE_SMOOTH)));
                    labels.add(l);
                    String s = file.getName();
                    s = switch (s) {
                        case "blue_token.png" -> TokenColor.BLUE.toString().toLowerCase();
                        case "green_token.png" -> TokenColor.GREEN.toString().toLowerCase();
                        case "red_token.png" -> TokenColor.RED.toString().toLowerCase();
                        case "yellow_token.png" -> TokenColor.YELLOW.toString().toLowerCase();
                        default -> s;
                    };
                    tokenNames.add(s);
                    JCheckBox c = new JCheckBox(s);
                    c.setFocusPainted(false);
                    c.setBorderPainted(false);
                    c.setForeground(container.getBackground());
                    checkBoxes.add(c);
                    System.out.println(file.getName());
                }
            } else System.out.println("Errore1");
        } else System.out.println("Errore2");

        for(JLabel l: labels) tokenPanel.add(l);


        gbc.gridy = 2;
        JPanel namePanel = new JPanel(new FlowLayout());
        namePanel.setOpaque(false);
        for(String s: tokenNames){
            JLabel l = new JLabel(s);
            l.setFont(new Font("Old English Text MT", Font.BOLD, 32));
            Insets insets = new Insets(30, 124, 10, 120);
            l.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
            namePanel.add(l);
        }
        container.add(namePanel, gbc);

        gbc.gridy = 3;
        JPanel checkBoxPanel = new JPanel(new FlowLayout());
        checkBoxPanel.setOpaque(false);
        ButtonGroup buttonGroup = new ButtonGroup();

        for(JCheckBox c : checkBoxes){
            buttonGroup.add(c);
            Insets insets = new Insets(0, 140, 70, 125);
            c.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
            c.setOpaque(false);
            checkBoxPanel.add(c);
        }
        container.add(checkBoxPanel, gbc);


        gbc.gridy = 4;
        JButton confirmButton = new JButton("Confirm");
        confirmButton.setFont(new Font("Old English Text MT", Font.BOLD, 30));
        container.add(confirmButton, gbc);

        ActionListener actionListener = e -> confirmButton.setEnabled(checkBoxes.stream().anyMatch(AbstractButton::isSelected));

        for(JCheckBox c : checkBoxes) c.addActionListener(actionListener);

        confirmButton.addActionListener (e -> {
            String tokenColorChosen;
            for (JCheckBox checkBox : checkBoxes) {
                if (checkBox.isSelected()) {
                    tokenColorChosen = checkBox.getText();
                    JOptionPane.showMessageDialog(container, "Hai selezionato: " + tokenColorChosen); //for debugging
                    //virtualServer.chooseToken(TokenColor.valueOf(tokenColorChosen.toUpperCase()));
                    break;
                }
            }
            //dispose();
        });
        setVisible(true);
    }



}
