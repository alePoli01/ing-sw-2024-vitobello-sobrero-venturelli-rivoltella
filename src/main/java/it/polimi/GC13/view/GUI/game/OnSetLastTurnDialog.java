package it.polimi.GC13.view.GUI.game;

import it.polimi.GC13.enums.Position;
import it.polimi.GC13.view.GUI.FrameManager;
import it.polimi.GC13.view.GUI.ImageIconRenderer;
import it.polimi.GC13.view.GUI.ImageIconTableModel;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.JTableHeader;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class OnSetLastTurnDialog extends JDialog {
    private FrameManager frameManager;

    public OnSetLastTurnDialog(FrameManager frameManager, String nickname, Position playerPosition) {
        super(frameManager, "Last Round", true);
        setSize(400, 300);
        setResizable(false);
        this.frameManager = frameManager;

        setLayout(new GridBagLayout());

        JLabel messageLabel = createTextLabelFont(nickname + " has reached 20 points. The will be another turn for players in position: ", 25);
        add(messageLabel, createGridBagConstraints(0,0));

        JPanel tablePanel = new JPanel();
        String[] columnNames = {" ", "Position", "Player"};
        Object[][] data = {

        };



        JTable table = createTable(columnNames, data);
        addScrollPane(table, tablePanel, 200, 285);

        add(tablePanel, createGridBagConstraints(0,1));


        JButton closeButton = new JButton("OK");
        closeButton.addActionListener(e -> {
            dispose();
        });

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

    private void setCompoundBorderInsets(JComponent jComponent, int insetsTop, int insetsLeft, int insetsBottom, int insetsRight, String inset, Color color, int thickness) {
        Insets insets = new Insets(insetsTop, insetsLeft, insetsBottom, insetsRight);
        Border b = BorderFactory.createEmptyBorder(insets.top, insets.left, insets.bottom, insets.right);
        switch (inset) {
            case "TOP" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(1, 0, 0, 0, Color.black)));
            case "BOTTOM" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createMatteBorder(0, 0, 1, 0, Color.black)));
            case "ALL" -> jComponent.setBorder(BorderFactory.createCompoundBorder(b, BorderFactory.createLineBorder(color, thickness)));
        }
    }

    private static JTable createTable(String[] columnNames, Object[][] data) {
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
    }


}
