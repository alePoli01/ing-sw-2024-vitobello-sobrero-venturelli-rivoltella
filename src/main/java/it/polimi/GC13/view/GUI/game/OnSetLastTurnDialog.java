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


public class OnSetLastTurnDialog extends JDialog implements CardManager{
    public record ColorRecord(TokenColor token, int[] rgb){}
    private Map<String, ColorRecord> playerToColorMap = new HashMap<>();
    boolean b = false;

    public OnSetLastTurnDialog(FrameManager frameManager, String nickname) {
        super(frameManager, "Last Round", true);
        setSize(500, 250);
        setResizable(false);
        setLocationRelativeTo(frameManager);

        setLayout(new BorderLayout());

        for(Map.Entry<String, TokenColor> entry : frameManager.getTokenInGame().entrySet()){
            ColorRecord record = new ColorRecord(entry.getValue(), remappingTokenToColor(entry.getValue()));
            playerToColorMap.put(entry.getKey(), record);
        }


        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);

        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ERROR_IMAGE, 100)); //DA CAMBIARE L'IMMAGINE
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 0);
        panelContainer.add(labelImage, createGridBagConstraints(0,0));

        JPanel panel = new JPanel(new GridBagLayout());
        panelContainer.add(panel, createGridBagConstraints(1,0));

        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.LINE_START;


        EnumMap<Position, String> positionToPlayerMap = new EnumMap<>(Position.class);

        for(Map.Entry<String, Position> entry : frameManager.getPlayerPositions().entrySet()){
            positionToPlayerMap.put(entry.getValue(), entry.getKey());
        }


        String text = String.format("<span style='color: rgb(%d, %d, %d); font-size: 20px;'>" + nickname + "</span>", playerToColorMap.get(nickname).rgb[0], playerToColorMap.get(nickname).rgb[1], playerToColorMap.get(nickname).rgb[2]);
        JLabel messageLabel1 = createTextLabelFont( "<html>" + text + " has reached 20 points. </html>", 25);
        setBorderInsets(messageLabel1, 0,0,15,0);
        panel.add(messageLabel1, gbc);
        gbc.gridy++;

        int lastTurnSetterPosition = frameManager.getPlayerPositions().get(nickname).getIntPosition();
        Position positionNickname = frameManager.getPlayerPositions().get(nickname).next(frameManager.getPlayerPositions().size());

        if (lastTurnSetterPosition < frameManager.getPlayerPositions().size()) {
            JLabel messageLabel3 = createTextLabelFont( "There will be another turn for players: ", 20);
            setBorderInsets(messageLabel3, 0,0,5,0);
            panel.add(messageLabel3, gbc);
            gbc.gridy++;
        }

        while (lastTurnSetterPosition < frameManager.getPlayerPositions().size()) {
            JLabel label = createTextLabelFont(" -->" + positionToPlayerMap.get(positionNickname) + " \n", 16);
            setBorderInsets(label, 0,0,5,0);
            panel.add(label, gbc);
            gbc.gridy++;
            positionNickname = positionNickname.next(frameManager.getPlayerPositions().size());
            lastTurnSetterPosition++;
        }

        String thenText = "Then ";
        String nowText = "Now";

        JLabel finalLabel = createTextLabelFont("<html>" + (b ? thenText : nowText) + "it will start the <u>last round</u>." + "</html>", 20);
        setBorderInsets(finalLabel, 10,0,0,0);
        panel.add(finalLabel, gbc);

        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);
    }


    //STATICO
    /*public OnSetLastTurnDialog(JFrame parent, String nickname) {
        super(parent, "Last Round", true);
        setSize(500, 250);
        setResizable(false);
        setLocationRelativeTo(parent);

        Map<String, Position> playersPosition = new HashMap<>(); //DA NON METTERE

        playersPosition.put("Nico", Position.FIRST);
        playersPosition.put("Nico2", Position.SECOND);
        playersPosition.put("Nico3", Position.THIRD);
        playersPosition.put("Nico4", Position.FOURTH);

        Map<String, TokenColor> tokenInGame = new HashMap<>();

        int i = 0;
        for(Map.Entry<String, Position> entry : playersPosition.entrySet()){ //DA NON METTERE
            tokenInGame.put(entry.getKey(), Arrays.stream(TokenColor.values()).toList().get(i));
            i++;
        }


        for(Map.Entry<String, TokenColor> entry : tokenInGame.entrySet()){
            ColorRecord record = new ColorRecord(entry.getValue(), remappingTokenToColor(entry.getValue()));
            playerToColorMap.put(entry.getKey(), record);
        }



        setLayout(new BorderLayout());

        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);


        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ERROR_IMAGE, 100));
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 0);
        panelContainer.add(labelImage, createGridBagConstraints(0,0));

        JPanel panel = new JPanel(new GridBagLayout());
        panelContainer.add(panel, createGridBagConstraints(1,0));

        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.LINE_START;


        EnumMap<Position, String> positionToPlayerMap = new EnumMap<>(Position.class);

        for(Map.Entry<String, Position> entry : playersPosition.entrySet()){
            positionToPlayerMap.put(entry.getValue(), entry.getKey());
        }

        System.out.println(playerToColorMap.get(nickname).token);

        String text = String.format("<span style='color: rgb(%d, %d, %d); font-size: 20px;'>" + nickname + "</span>", playerToColorMap.get(nickname).rgb[0], playerToColorMap.get(nickname).rgb[1], playerToColorMap.get(nickname).rgb[2]);
        JLabel messageLabel1 = createTextLabelFont( "<html>" + text + " has reached 20 points. </html>", 25);
        //JLabel messageLabel1 = createTextLabelFont( "<html><span style='color: red; font-size: 20px;'>" + nickname + "</span> has reached 20 points. </html>", 25);
        setBorderInsets(messageLabel1, 0,0,15,0);
        panel.add(messageLabel1, gbc);
        gbc.gridy++;


        int lastTurnSetterPosition = playersPosition.get(nickname).getIntPosition();
        Position positionNickname = playersPosition.get(nickname).next(playersPosition.size());

        if (lastTurnSetterPosition < playersPosition.size()) {
            JLabel messageLabel3 = createTextLabelFont( "There will be another turn for players: ", 20);
            setBorderInsets(messageLabel3, 0,0,5,0);
            panel.add(messageLabel3, gbc);
            gbc.gridy++;
        }


        while (lastTurnSetterPosition < playersPosition.size()) {
            JLabel label = createTextLabelFont(" -->" + positionToPlayerMap.get(positionNickname) + " \n", 16);
            setBorderInsets(label, 0,0,5,0);
            panel.add(label, gbc);
            gbc.gridy++;
            positionNickname = positionNickname.next(playersPosition.size());
            lastTurnSetterPosition++;
        }

        String thenText = "Then ";
        String nowText = "Now";

        JLabel finalLabel = createTextLabelFont("<html>" + (b ? thenText : nowText) + "it will start the <u>last round</u>." + "</html>", 20);
        setBorderInsets(finalLabel, 10,0,0,0);
        panel.add(finalLabel, gbc);

        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);
    }*/

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
                return new int[]{107, 189, 192};
            }
            case RED -> {
                return new int[]{233, 73, 23};
            }
            case GREEN -> {
                return new int[]{113,192, 124};
            }
            case YELLOW -> {
                return new int[]{255, 240, 1};
            }
            default -> {
                return new int[]{0,0,0};
            }
        }
    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException {}

}
