package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.enums.Resource;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.GUI.ImageIconRenderer;
import it.polimi.GC13.view.GUI.ImageIconTableModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnSetLastTurnDialog extends JDialog implements CardManager{
    private FrameManager frameManager;
    private MainPage gamePage;


    //poi sarà framemanager il parent
    /*public OnSetLastTurnDialog(FrameManager parent, MainPage gamePage, String nickname, Position playerPosition) {
        super(parent, "Last Round", true);
        setSize(500, 200);
        setResizable(false);
        setLocationRelativeTo(null);
        this.frameManager = parent;
        this.gamePage = gamePage;

        setLayout(new BorderLayout());

        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);


        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ERROR_IMAGE, 100));
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 2);
        panelContainer.add(labelImage, createGridBagConstraints(0,0));

        JPanel panel = new JPanel(new GridBagLayout());
        panelContainer.add(panel, createGridBagConstraints(1,0));


        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel messageLabel1 = createTextLabelFont( "<html><span style='color: red; font-size: 20px;'>" + nickname + "</span> has reached 20 points. </html>", 18);
        panel.add(messageLabel1, gbc);
        gbc.gridy++;
        JLabel messageLabel2 = createTextLabelFont( "There will be another turn for players: ", 18);
        panel.add(messageLabel2, gbc);
        gbc.gridy++;


        //da togliere
        Map<String, Position> playerPositions = new HashMap<>();

        playerPositions.put("Nico", Position.FIRST);
        playerPositions.put("Nico2", Position.SECOND);
        playerPositions.put("Nico3", Position.THIRD);
        playerPositions.put("Nico4", Position.FOURTH);


        do {
            playerPosition = playerPosition.next(playerPositions.size());
            JLabel label = createTextLabelFont(" -->" + playerPosition.toString().toLowerCase() + " \n", 16);
            panel.add(label, gbc);
            gbc.gridy++;
        } while (playerPosition != Position.FOURTH);
        JLabel finalLabel = createTextLabelFont(" and another one bonus for everyone.", 20);
        panel.add(finalLabel, gbc);




        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);




    }*/


    public OnSetLastTurnDialog(JFrame parent, String nickname, Position playerPosition) {
        super(parent, "Last Round", true);
        setSize(500, 200);
        setResizable(false);
        setLocationRelativeTo(parent);

        setLayout(new BorderLayout());

        JPanel panelContainer = new JPanel(new GridBagLayout());
        add(panelContainer, BorderLayout.CENTER);


        JLabel labelImage = new JLabel(createResizedTokenImageIcon(ERROR_IMAGE, 100));
        setCompoundBorderInsets(labelImage, 10,10,10,10, "ALL", Color.BLACK, 2);
        panelContainer.add(labelImage, createGridBagConstraints(0,0));

        JPanel panel = new JPanel(new GridBagLayout());
        panelContainer.add(panel, createGridBagConstraints(1,0));


        GridBagConstraints gbc = createGridBagConstraints(0,0);
        gbc.anchor = GridBagConstraints.LINE_START;

        JLabel messageLabel1 = createTextLabelFont( "<html><span style='color: red; font-size: 20px;'>" + nickname + "</span> has reached 20 points. </html>", 18);
        panel.add(messageLabel1, gbc);
        gbc.gridy++;
        JLabel messageLabel2 = createTextLabelFont( "There will be another turn for players: ", 18);
        panel.add(messageLabel2, gbc);
        gbc.gridy++;

        Map<String, Position> playerPositions = new HashMap<>();

        playerPositions.put("Nico", Position.FIRST);
        playerPositions.put("Nico2", Position.SECOND);
        playerPositions.put("Nico3", Position.THIRD);
        playerPositions.put("Nico4", Position.FOURTH);


        do {
            playerPosition = playerPosition.next(playerPositions.size());
            JLabel label = createTextLabelFont(" -->" + playerPosition.toString().toLowerCase() + " \n", 16);
            panel.add(label, gbc);
            gbc.gridy++;
        } while (playerPosition != Position.FOURTH);
        JLabel finalLabel = createTextLabelFont(" and another one bonus for everyone.", 20);
        panel.add(finalLabel, gbc);




        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, BorderLayout.SOUTH);


    }







    private void addScrollPane(JComponent component, JPanel panel, int dimW, int dimH){
        JScrollPane scrollPane = new JScrollPane(component);
        scrollPane.setPreferredSize(new Dimension(dimW, dimH));
        panel.add(scrollPane);
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


//    public void createResourceTable(JTable table, String[] columnsNames, EnumMap<Position, Integer> positionMap) {
//        ImageIconTableModel<Position, Integer> model = new ImageIconTableModel<>(columnsNames, positionMap);
//
//
//        table.setModel(model);
//        table.getColumnModel().getColumn(0).setCellRenderer(new ImageIconRenderer());
//
//        JTableHeader header = table.getTableHeader();
//        header.setPreferredSize(new Dimension(header.getWidth(), 30));
//
//        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
//        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
//        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);
//
//        table.setRowHeight(30);
//    }

    @Override
    public void showStarterCardAndPrivateObjectiveCard(List<Integer> hand) throws IOException {

    }
}
