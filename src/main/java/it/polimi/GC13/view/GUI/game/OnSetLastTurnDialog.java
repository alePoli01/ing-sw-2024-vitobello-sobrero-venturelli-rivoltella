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
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class OnSetLastTurnDialog extends JDialog {
    private FrameManager frameManager;
    private MainPage gamePage;

    //poi sarà framemanager il parent
    public OnSetLastTurnDialog(FrameManager parent, MainPage gamePage, String nickname, Position playerPosition) {
        super(parent, "Last Round", true);
        setSize(600, 300);
        setResizable(false);
        setLocationRelativeTo(null);
        this.frameManager = parent;
        this.gamePage = gamePage;

        setLayout(new GridBagLayout());

        JLabel messageLabel = createTextLabelFont(nickname + " has reached 20 points. There will be another turn for players in position: ", 25);
        add(messageLabel, createGridBagConstraints(0,0));

        int i=1;
        for (; playerPosition.ordinal() < frameManager.getPlayerPositions().size(); playerPosition.next(frameManager.getPlayerPositions().size())) {
            JLabel label = createTextLabelFont(playerPosition + " and another one bonus.", 27);
            add(messageLabel, createGridBagConstraints(0,i));
            i++;
        }



        JPanel tablePanel = new JPanel();
        tablePanel.setOpaque(false);
        String[] columnNames = {"Player", "Score" };

        EnumMap<Position, Integer> positionPlayerMap = new EnumMap<>(Position.class);

        for(Map.Entry<String, Position> entry : frameManager.getPlayerPositions().entrySet()){
            positionPlayerMap.put(entry.getValue(), frameManager.getPlayersScore().get(entry.getKey()));
        }

        JTable table = new JTable();
        createResourceTable(table, columnNames, positionPlayerMap);
        addScrollPane(table, tablePanel, 200, 285);

        //add(tablePanel, createGridBagConstraints(0,5));


        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

    }


    /*public OnSetLastTurnDialog(JFrame parent, String nickname, Position playerPosition) {
        super(parent, "Last Round", true);
        setSize(600, 300);
        setResizable(false);
        setLocationRelativeTo(parent);

        setLayout(new GridBagLayout());

        JLabel messageLabel = createTextLabelFont(nickname + " has reached 20 points. There will be another turn for players in position: ", 25);
        add(messageLabel, createGridBagConstraints(0,0));

        Map<String, Position> playerPositions = new HashMap<>();

        playerPositions.put("Nico", Position.FIRST);
        playerPositions.put("Nico2", Position.SECOND);
        playerPositions.put("Nico3", Position.THIRD);
        playerPositions.put("Nico4", Position.FOURTH);

        // Esempio di nickname fornito
            String nickname = "Nico2";

            // Ottieni la posizione iniziale
            Position playerPosition = playerPositions.get(nickname);



        int i=1;
        for (playerPositions.get(nickname); playerPosition.ordinal() < playerPositions.size(); playerPosition.next(playerPositions.size())) {
            JLabel label = createTextLabelFont(playerPosition + " and another one bonus.", 27);
            add(label, createGridBagConstraints(0,i));
            i++;
        }


        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

        add(closeButton, createGridBagConstraints(0,8));


    }*/







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

    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset, Color color, int thickness) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(color, thickness)));
        }
    }


    public void createResourceTable(JTable table, String[] columnsNames, EnumMap<Position, Integer> positionMap) {
        ImageIconTableModel<Position, Integer> model = new ImageIconTableModel<>(columnsNames, positionMap);


        table.setModel(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageIconRenderer());

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 30));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.setRowHeight(30);
    }

    /*private static JTable createTable(String[] columnNames, Object[][] data) {
        ImageIconTableModel model = new ImageIconTableModel(data, columnNames);
        JTable table = new JTable(model);
        table.getColumnModel().getColumn(0).setCellRenderer(new ImageIconRenderer());

        JTableHeader header = table.getTableHeader();
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer);

        table.setRowHeight(35);

        return table;
    }*/


}
