package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.view.GUI.FrameManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Stream;

public class PopupDialog extends JDialog implements ActionListener, CardManager{
    FrameManager frameManager;
    JPanel iconPanel;

    Map<Integer, CardData> serialNumerCheckBoxMap = new HashMap<>();

    private record CardData(JLabel label, JCheckBox checkBox) {}

    public PopupDialog(JFrame owner, FrameManager frameManager) {
        super(owner, "", true);
        setSize(400, 300);
        setResizable(false);
        setLayout(new BorderLayout());
        this.frameManager = frameManager;
    }


    public void drawResourceDeckPopup (JFrame owner){
        JDialog dialog = this;
        dialog.setTitle("Draw Card");

        JLabel label = createTextLabelFont("Choose the card to withdraw: ", 32);

        iconPanel = new JPanel(new FlowLayout());
        for(Integer resourceCard : frameManager.getResourceCardsAvailable().keySet()){
            showStartCard(resourceCard);
        }






        JButton confirmButton = new JButton("Draw");
        confirmButton.setPreferredSize(new Dimension(150, 25));
        JButton closeButton = new JButton("Close");
        closeButton.setPreferredSize(new Dimension(150, 25));


        confirmButton.addActionListener(this);
        closeButton.addActionListener(this);

        JPanel buttonPanel = new JPanel(new FlowLayout());
        add(label, BorderLayout.NORTH);
        buttonPanel.add(confirmButton);
        buttonPanel.add(closeButton);
        add(buttonPanel, BorderLayout.SOUTH);

        setLocationRelativeTo(owner);

    }



    //TODO: da implementare l'azione del bottone "Pesca"
    @Override
    public void actionPerformed(ActionEvent e) {
        if(e.getActionCommand().equals("Pesca")){
            //aggiunge la carta alla mano
            dispose();
        }else if(e.getActionCommand().equals("Chiudi")){
            dispose(); //dovrebbe chiudere il popup senza fare niente, da verificare
        }
    }


    private GridBagConstraints createGridBagConstraints(int x, int y){
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.gridx = x;
        gbc.gridy = y;

        return gbc;
    }

    private JLabel createTextLabelFont(String content, int dim) {
        JLabel jLabel = new JLabel(content);
        jLabel.setFont(new Font("Old English Text MT", Font.BOLD, dim));
        return jLabel;
    }

    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset, Color color, int thickness) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(color, thickness)));
        }
    }

    public Path identifyPathCard(int numberCard){
        Path startDir;
        if(frameManager.getResourceCardsAvailable().get(numberCard)){
            startDir = Paths.get(RESOURCE_DIR + "resource_card_back/");
        } else {
            startDir = Paths.get(RESOURCE_DIR + "resource_card_front/");
        }
        return startDir;
    }

    public void showStartCard(Integer numberCard) {
     /*   Path startDir = identifyPathCard(numberCard);

        if(startDir != null){
            try (Stream<Path> paths = Files.walk(Paths.get(startDir.toUri())).filter(Files::isRegularFile).filter(f -> f.getFileName().toString().contains(numberCard.toString()))) {
                paths.forEach(path -> {
                    Image img = new ImageIcon(String.valueOf(path)).getImage();
                    JLabel cardLabelImage = new JLabel(new ImageIcon(img.getScaledInstance(img.getWidth(null)/3 , img.getHeight(null)/3, Image.SCALE_SMOOTH)));
                    setCompoundBorderInsets(cardLabelImage, 0, 100, 0, 100, "ALL", Color.BLACK, 1);
                    iconPanel.add(cardLabelImage);

                    JLabel startCardLabelText;
                    JCheckBox jCheckBox;
                    if(frameManager.getResourceCardsAvailable().get(numberCard)){
                        startCardLabelText = createTextLabelFont("Deck", 32);
                        jCheckBox = new JCheckBox("false");
                    } else {
                        startCardLabelText = createTextLabelFont("Back", 32);
                        jCheckBox = new JCheckBox("true");
                    }

                    setBorderInsets(startCardLabelText, 30, 230, 80, 230);
                    namePanel.add(startCardLabelText);

                    jCheckBox.setFocusPainted(false);
                    jCheckBox.setBorderPainted(false);
                    jCheckBox.setForeground(panelContainer.getBackground());
                    buttonGroup.add(jCheckBox);
                    setBorderInsets(jCheckBox, 0, 140, 70, 125);
                    jCheckBox.setOpaque(false);
                    jCheckBox.addActionListener(e -> {
                        confirmButton.setEnabled(labelCheckBoxMap.values().stream().anyMatch(AbstractButton::isSelected));

                        labelCheckBoxMap.keySet().forEach(k -> k.setForeground(Color.BLACK));

                        labelCheckBoxMap.entrySet()
                                .stream()
                                .filter(en -> en.getValue().equals(e.getSource()))
                                .findFirst()
                                .orElseThrow()
                                .getKey()
                                .setForeground(Color.RED);
                    });
                    checkBoxPanel.add(jCheckBox);
                    labelCheckBoxMap.put(startCardLabelText, jCheckBox);
                });

            } catch (IOException e) {
                JOptionPane.showMessageDialog(this, "ErrorMsg: " + e.getMessage(), "Invalid card", JOptionPane.ERROR_MESSAGE);
            }
        }*/
    }
}
