package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.TokenColor;
import it.polimi.GC13.view.GUI.FrameManager;

import javax.swing.*;
import javax.swing.border.Border;
import java.awt.*;

import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.stream.Collectors;


public class OnSetLastTurnDialog extends JDialog implements CardManager{
    public record ColorRecord(TokenColor token, int[] rgb){}

    public OnSetLastTurnDialog(FrameManager frameManager, String playerNickname) {
        super(frameManager, "Last Round", true);
        setResizable(false);
        setSize(600, 250);
        setLocationRelativeTo(frameManager);
        setLayout(new BorderLayout());

        Map<String, ColorRecord> playerToColorMap = new HashMap<>();

        for(Map.Entry<String, TokenColor> entry : frameManager.getTokenInGame().entrySet()){
            ColorRecord record = new ColorRecord(entry.getValue(), remappingTokenToColor(entry.getValue()));
            playerToColorMap.put(entry.getKey(), record);
        }


        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);

        //immagine
        JPanel imagePanel = new JPanel();
        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ADVERTISEMENT_MONK, 100));
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 0);
        imagePanel.add(labelImage);

        //labels
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraints = createGridBagConstraints(0,0);
        labelConstraints.weightx = 1.0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;


        String text = String.format("<span style = 'color: rgb(%d, %d, %d); font-size: 20px;'>" + playerNickname + "</span>", playerToColorMap.get(playerNickname).rgb[0], playerToColorMap.get(playerNickname).rgb[1], playerToColorMap.get(playerNickname).rgb[2]);
        JLabel messageLabel1 = createTextLabelFont( "<html>" + text + " has reached 20 points. </html>", 25);
        setBorderInsets(messageLabel1, 0,0,15,0);
        labelsPanel.add(messageLabel1, labelConstraints);
        labelConstraints.gridy++;

        String textMessage;
        if (frameManager.getPlayerPositions().get(playerNickname).getIntPosition() != frameManager.getPlayerPositions().size()) {
            String lastPlayer = "";
            Position lastPosition = Position.getPositionFromInt(frameManager.getPlayerPositions().size());

            for(String s : frameManager.getPlayerPositions().keySet()){
                if (frameManager.getPlayerPositions().get(s) == lastPosition) {
                    lastPlayer = s;
                    break;
                }
            }

            textMessage = "<html> After " + lastPlayer + "'s turn, the <u>last round</u> will start. </html>";
        } else {
            textMessage = "<html>After this turn, the <u>Last round</u> starts. </html>";
        }

        JLabel finalLabel = createTextLabelFont(textMessage, 20);
        setBorderInsets(finalLabel, 10,0,0,0);
        labelsPanel.add(finalLabel, labelConstraints);


        GridBagConstraints mainConstraints = createGridBagConstraints(0,0);
        panelContainer.add(imagePanel, mainConstraints);

        mainConstraints.gridx++;
        mainConstraints.fill = GridBagConstraints.BOTH;
        mainConstraints.weightx = 1.0;
        mainConstraints.weighty = 1.0;
        panelContainer.add(labelsPanel, mainConstraints);


        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);
    }


    private void setBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        jComponent.setBorder(BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right));
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

    private static ImageIcon createResizedTokenImageIcon(String tokenImagePath, int dim) {
        return new ImageIcon(new ImageIcon(tokenImagePath).getImage().getScaledInstance(dim, dim, Image.SCALE_SMOOTH));
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


    private int[] remappingTokenToColor(TokenColor token){
        switch (token){
            case BLUE -> {
                return new int[]{27, 78, 156};
            }
            case RED -> {
                return new int[]{233, 30, 23};
            }
            case GREEN -> {
                return new int[]{21, 158, 41};
            }
            case YELLOW -> {
                return new int[]{177, 170, 9};
            }
            default -> {
                return new int[]{0,0,0};
            }
        }
    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException {}
















    //STATICO
    public OnSetLastTurnDialog(MainFrameProva frameManager, String playerNickname) {
        super(frameManager, "Last Round", true);
        setResizable(false);
        setSize(600, 250);
        setLocationRelativeTo(frameManager);
        setLayout(new BorderLayout());

        Map<String, ColorRecord> playerToColorMap = new HashMap<>();

        for(Map.Entry<String, TokenColor> entry : frameManager.getTokenInGame().entrySet()){
            ColorRecord record = new ColorRecord(entry.getValue(), remappingTokenToColor(entry.getValue()));
            playerToColorMap.put(entry.getKey(), record);
        }


        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);

        //immagine
        JPanel imagePanel = new JPanel();
        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ADVERTISEMENT_MONK, 195));
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 0);
        imagePanel.add(labelImage);

        //labels
        JPanel labelsPanel = new JPanel(new GridBagLayout());
        GridBagConstraints labelConstraints = createGridBagConstraints(0,0);
        labelConstraints.weightx = 1.0;
        labelConstraints.fill = GridBagConstraints.HORIZONTAL;
        labelConstraints.anchor = GridBagConstraints.LINE_START;


        Map<Position, String> positionPlayerMap = frameManager.getPlayerPosition().entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.comparingInt(Position::getIntPosition)))
                .collect(Collectors.toMap(Map.Entry::getValue, Map.Entry::getKey,
                        (e1, e2) -> e1,
                        () -> new EnumMap<>(Position.class)));


        String text = String.format("<span style='color: rgb(%d, %d, %d); font-size: 20px;'>" + playerNickname + "</span>", playerToColorMap.get(playerNickname).rgb[0], playerToColorMap.get(playerNickname).rgb[1], playerToColorMap.get(playerNickname).rgb[2]);
        JLabel messageLabel1 = createTextLabelFont( "<html>" + text + " has reached 20 points. </html>", 25);
        setBorderInsets(messageLabel1, 0,0,15,0);
        labelsPanel.add(messageLabel1, labelConstraints);
        labelConstraints.gridy++;


        String textMessage;
        if (!frameManager.getPlayerPosition().get(playerNickname).equals(Position.FOURTH) ) {
            String fourthPlayer = positionPlayerMap.get(Position.FOURTH);
            textMessage = "<html> After " + fourthPlayer + "'s turn, the <u>last round</u> will start. </html>";
        } else {
            textMessage = "<html><u>Last round</u> is starting... NOW! </html>";
        }

        JLabel finalLabel = createTextLabelFont(textMessage, 20);
        setBorderInsets(finalLabel, 10,0,0,0);
        labelsPanel.add(finalLabel, labelConstraints);


        GridBagConstraints mainConstraints = createGridBagConstraints(0,0);
        panelContainer.add(imagePanel, mainConstraints);

        mainConstraints.gridx++;
        mainConstraints.fill = GridBagConstraints.BOTH;
        mainConstraints.weightx = 1.0;
        mainConstraints.weighty = 1.0;
        panelContainer.add(labelsPanel, mainConstraints);


        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);
    }


}
